package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressRepository;
import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainRepository;
import com.serpents.ipv6dns.exception.ObjectNotInTheCorrectStateException;
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
    private final AddressRepository addressRepository;

    @Autowired
    public DomainRequestsService(final DomainRequestsRepository requestsRepository, final DomainRepository domainRepository, final AddressRepository addressRepository) {
        this.requestsRepository = requestsRepository;
        this.domainRepository = domainRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public DomainRequestResponse requestDomain(final UUID clientId, final DomainDetails details) {
        final DomainRequest request = new DomainRequest(clientId, SENT, details);
        return requestsRepository.insert(request);
    }

    @Transactional
    public void cancelRequest(final UUID requestId, final UUID clientId) {
        final DomainRequest request = requestsRepository.findById(requestId);
        if (!clientId.equals(request.getClientId())) {
            throw new UnauthorizedOperationException("Request can't be cancelled");
        } else {
            updateRequest(requestId, CANCELED);
        }
    }

    @Transactional
    public DomainCreatedResponse approveRequest(final UUID requestId, final DomainRequestApproval approval) {
        final Address address = approval.getAddress().orElseGet(addressRepository::findFreeAddress);
        final DomainRequest request = requestsRepository.findById(requestId);
        final Domain domain = new Domain(request, address);

        updateRequest(requestId, APPROVED);
        return domainRepository.insert(domain);
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
