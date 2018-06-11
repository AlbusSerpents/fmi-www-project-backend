package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressesRepository;
import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainRepository;
import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;
import com.serpents.ipv6dns.exception.InvalidDomainNameException;
import com.serpents.ipv6dns.exception.ObjectNotInTheCorrectStateException;
import com.serpents.ipv6dns.exception.OutOfAddressSpaceException;
import com.serpents.ipv6dns.exception.UnauthorizedOperationException;
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

    @Autowired
    public DomainRequestsService(final DomainRequestsRepository requestsRepository,
                                 final DomainRepository domainRepository,
                                 final AddressesRepository addressesRepository,
                                 final ApplicationProperties properties) {
        this.requestsRepository = requestsRepository;
        this.domainRepository = domainRepository;
        this.addressesRepository = addressesRepository;
        this.domainNamePattern = domainPatter(properties);
    }

    private Pattern domainPatter(final ApplicationProperties properties) {
        final String zoneName = properties.getProperty(DNS_ZONE_NAME);
        return compile("(www\\.)?([0-9a-zA-Z-_]+)(" + zoneName + ")");
    }

    @Transactional
    public DomainRequestResponse requestDomain(final UUID clientId, final DomainDetails details) {
        final String domainName = details.getDomainName();
        if (!domainNamePattern.matcher(domainName).matches()) {
            throw new InvalidDomainNameException(domainName + " is not a valid domain name");
        }
        final DomainRequest request = new DomainRequest(clientId, SENT, details);
        return requestsRepository.insert(request);
    }

    @Transactional
    public List<DomainRequest> listPending() {
        return requestsRepository.findPending();
    }

    @Transactional
    public DomainRequest readByIdentifier(final Identifier identifier) {
        final UUID requestId = identifier.getRequestId();
        final UUID clientId = identifier.getClientId();
        final DomainRequest request = requestsRepository.findById(requestId);

        if (request.getClientId().equals(clientId)) {
            return request;
        } else {
            final String message = String.format("Request %s not found", requestId);
            throw new UnauthorizedOperationException(message);
        }
    }

    @Transactional
    public void cancel(final Identifier identifier) {
        final boolean success = requestsRepository.cancel(identifier);
        if (!success) {
            final String message = String.format("Couldn't cancel request %s ", identifier.getRequestId());
            throw new UnauthorizedOperationException(message);
        }
    }

    @Transactional
    public DomainCreatedResponse approve(final UUID requestId, final DomainRequestApproval approval) {
        final Address address = approval
                .getAddressId()
                .map(addressesRepository::findById)
                .orElseGet(this::freeAddress);

        final DomainRequest request = requestsRepository.findById(requestId);
        final Domain domain = new Domain(request, address);

        approveRequest(requestId);
        return domainRepository.insert(domain);
    }

    private Address freeAddress() {
        return addressesRepository.findFree().orElseThrow(OutOfAddressSpaceException::new);
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
