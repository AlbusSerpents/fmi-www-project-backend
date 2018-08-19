package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressesRepository;
import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainRepository;
import com.serpents.ipv6dns.exception.InvalidDomainNameException;
import com.serpents.ipv6dns.exception.ObjectNotInTheCorrectStateException;
import com.serpents.ipv6dns.exception.OutOfAddressSpaceException;
import com.serpents.ipv6dns.spring.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.SENT;
import static com.serpents.ipv6dns.spring.properties.ApplicationPropertiesKey.DNS_ZONE_NAME;
import static java.util.regex.Pattern.compile;

@Service
public class DomainRequestsService {

    private final DomainRequestsRepository requestsRepository;
    private final DomainRepository domainRepository;
    private final AddressesRepository addressesRepository;
    private final Pattern domainNamePattern;
    private final Pattern fullDomainNamePattern;

    @Autowired
    public DomainRequestsService(final DomainRequestsRepository requestsRepository,
                                 final DomainRepository domainRepository,
                                 final AddressesRepository addressesRepository,
                                 final ApplicationProperties properties) {
        this.requestsRepository = requestsRepository;
        this.domainRepository = domainRepository;
        this.addressesRepository = addressesRepository;
        this.domainNamePattern = domainPatter(properties);
        this.fullDomainNamePattern = fullDomainNamePatter(properties);
    }

    private Pattern domainPatter(final ApplicationProperties properties) {
        final String zoneName = properties.getProperty(DNS_ZONE_NAME);
        return compile("([0-9a-zA-Z\\-_]+)(" + zoneName + ")");
    }

    private Pattern fullDomainNamePatter(final ApplicationProperties properties) {
        final String zoneName = properties.getProperty(DNS_ZONE_NAME);
        return compile("(www\\.)?([0-9a-zA-Z\\-_]+)(" + zoneName + ")");
    }

    @Transactional
    public DomainRequestResponse requestDomain(final UUID clientId, final DomainDetails details) {
        final String domainName = details.getDomainName();
        final String realDomainName = validateDomainName(domainName);
        final DomainDetails realDetails = new DomainDetails(details.getId(), realDomainName, details.getDescription());
        final DomainRequest request = new DomainRequest(clientId, SENT, realDetails);
        return requestsRepository.insert(request);
    }

    private String validateDomainName(final String domainName) {
        if (fullDomainNamePattern.matcher(domainName).matches()) {
            return domainName.substring(4);
        } else if (domainNamePattern.matcher(domainName).matches()) {
            return domainName;
        } else {
            final String message = String.format("%s is not a valid domain name", domainName);
            throw new InvalidDomainNameException(message);
        }
    }

    @Transactional
    public List<DomainRequestInfo> listPending() {
        return requestsRepository.findPending();
    }

    @Transactional
    public DomainCreatedResponse approve(final UUID requestId) {
        final Address address = addressesRepository
                .findFree()
                .orElseThrow(OutOfAddressSpaceException::new);

        final DomainRequestInfo info = requestsRepository.findById(requestId);
        final Domain domain = new Domain(info, address);

        approveRequest(requestId);
        return domainRepository.insert(domain);
    }

    private void approveRequest(final UUID requestId) {
        final boolean succeeded = requestsRepository.approve(requestId);
        if (!succeeded) {
            final String message = String.format("Couldn't approve request: %s", requestId);
            throw new ObjectNotInTheCorrectStateException(message);
        }
    }

    @Transactional
    public void rejectRequest(final UUID requestId) {
        final boolean success = requestsRepository.reject(requestId);
        if (!success) {
            final String message = String.format("Couldn't reject request %s", requestId);
            throw new RuntimeException(message);
        }
    }

}
