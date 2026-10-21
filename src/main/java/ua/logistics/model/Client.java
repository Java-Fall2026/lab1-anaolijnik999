package ua.logistics.model;

import ua.logistics.util.LogisticsUtils;

public record Client(String taxId, String companyName, String contactPhone) {
    public Client {
        LogisticsUtils.validateNotBlank(taxId, "Tax ID");
        LogisticsUtils.validateNotBlank(companyName, "Company name");
        LogisticsUtils.validateNotBlank(contactPhone, "Contact phone");
    }
}