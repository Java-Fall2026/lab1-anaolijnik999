package ua.logistics.model;

import ua.common.BaseEntity;
import ua.logistics.util.LogisticsUtils;

import java.util.Objects;

/**
 * Client entity with private constructor and static factory method.
 */
public class Client extends BaseEntity {

    private final String taxId;
    private final String companyName;
    private final String contactPhone;

    private Client(String taxId, String companyName, String contactPhone) {
        super();
        LogisticsUtils.validateNotBlank(taxId, "Tax ID");
        LogisticsUtils.validateNotBlank(companyName, "Company name");
        LogisticsUtils.validateNotBlank(contactPhone, "Contact phone");

        this.taxId = taxId;
        this.companyName = companyName;
        this.contactPhone = contactPhone;
    }

    public static Client of(String taxId, String companyName, String contactPhone) {
        return new Client(taxId, companyName, contactPhone);
    }

    public String getTaxId() {
        return taxId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(taxId, client.taxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxId);
    }

    @Override
    public String toString() {
        return "Client{" +
                "taxId='" + taxId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}