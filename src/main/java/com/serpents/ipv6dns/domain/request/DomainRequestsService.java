package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressesRepository;
import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainRepository;
import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;
import com.serpents.ipv6dns.exception.ObjectNotInTheCorrectStateException;
import com.serpents.ipv6dns.exception.OutOfAddressSpaceException;
import com.serpents.ipv6dns.exception.UnauthorizedOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.SENT;

@Service
public class DomainRequestsService {


    private final DomainRequestsRepository requestsRepository;
    private final DomainRepository domainRepository;
    private final AddressesRepository addressesRepository;

    @Autowired
    public DomainRequestsService(final DomainRequestsRepository requestsRepository, final DomainRepository domainRepository, final AddressesRepository addressesRepository) {
        this.requestsRepository = requestsRepository;
        this.domainRepository = domainRepository;
        this.addressesRepository = addressesRepository;
    }

    @Transactional
    public DomainRequestResponse requestDomain(final UUID clientId, final DomainDetails details) {
        final DomainRequest request = new DomainRequest(clientId, SENT, details);
        return requestsRepository.insert(request);
    }

    @Transactional
    public DomainRequest readByIdentifier(final Identifier identifier) {
        return requestsRepository.findByIdentifier(identifier);
    }

    @Transactional
    public void cancelRequest(final Identifier identifier) {
        final boolean success = requestsRepository.cancel(identifier);
        if (!success) {
            final String message = String.format("Couldn't cancel request %s ", identifier.getRequestId());
            throw new UnauthorizedOperationException(message);
        }
    }

    @Transactional
    public DomainCreatedResponse approveRequest(final UUID requestId, final DomainRequestApproval approval) {
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

    @Transactional
    public void rejectRequest(final UUID requestId) {
        final boolean success = requestsRepository.reject(requestId);
        if (!success) {
            final String message = String.format("Couldn't reject request %s", requestId);
            throw new RuntimeException(message);
        }
    }

    private void approveRequest(final UUID requestId) {
        final boolean succeeded = requestsRepository.approve(requestId);
        if (!succeeded) {
            final String message = String.format("Couldn't approve request: %s", requestId);
            throw new ObjectNotInTheCorrectStateException(message);
        }
    }

}
