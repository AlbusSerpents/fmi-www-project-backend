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

import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.*;

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
        final DomainRequest request = requestsRepository.findByIdentifier(identifier);
        if (request != null) {
            updateRequest(identifier.getRequestId(), CANCELED);
        } else {
            throw new UnauthorizedOperationException("Request can't be cancelled");
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

        updateRequest(requestId, APPROVED);
        return domainRepository.insert(domain);
    }

    private Address freeAddress() {
        return addressesRepository.findFree().orElseThrow(OutOfAddressSpaceException::new);
    }

    @Transactional
    public void rejectRequest(final UUID requestId) {
        updateRequest(requestId, REJECTED);
    }

    private void updateRequest(final UUID requestId, final DomainRequestStatus status) {
        final boolean succeeded = requestsRepository.updateStatus(requestId, status);
        if (!succeeded) {
            final String message = String.format("Couldn't update request: %s with status: %s", requestId.toString(), status.toString());
            throw new ObjectNotInTheCorrectStateException(message);
        }
    }

}
