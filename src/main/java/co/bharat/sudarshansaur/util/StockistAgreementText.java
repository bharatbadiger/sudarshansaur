package co.bharat.sudarshansaur.util;

import co.bharat.sudarshansaur.entity.Address;
import co.bharat.sudarshansaur.entity.Stockists;

import java.util.Calendar;
import java.util.Date;

public class StockistAgreementText {
    public static String para1(Stockists stockists) {
        Date createdAt = stockists.getCreatedOn();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        return "This Agreement is executed on this " + calendar.get(Calendar.DAY_OF_MONTH) + " day of " + (calendar.get(Calendar.MONTH) + 1) + " Month " + calendar.get(Calendar.YEAR) + " Year by & between M/s Sudarshan Saur Shakti Pvt. Ltd. a Company Registered under Companies Act 1956 (1 of 1956) having their Registered Office at 5, Tarak Colony, Opp. Ramakrishna Mission Ashrama, Beed Bypass Road, Aurangabad – 431 005 Maharashtra. (Hereinafter referred as “the Company” of the First part)\n";
    }

    public static String para2(Stockists stockists) {
        Address address = stockists.getAddress();
        StringBuilder para = new StringBuilder();
        para.append(stockists.getStockistName() +", "+stockists.getBusinessName() + "\n");
        para.append("With the permanent Address As: - \n");
        para.append(getFullAddress(address));
        para.append("\n");
        para.append(stockists.getGstNumber());
        para.append("\n\n");
        System.out.println(para);
        return para.toString();
    }

    public static String para3() {
        return "\nDealer means any person, company, firm, Dealer, Sub-dealer, Distributor, Stockiest, Super-Stockiest, or any entity who is authorized by “the Company” (with or without agreement inwriting) to sell solar water heating systems directly or indirectly to the Customer/end user.\n";
    }

    public static String para4(Stockists dealer) {
        StringBuilder para = new StringBuilder();
        para.append("\nWHEREAS, a Company appoints\n");
        para.append("M/s. " + dealer.getBusinessName() + ",\n");
        para.append(dealer.getAddress().getTown() + "\n");
        para.append("as their Dealer\n");
        return para.toString();
    }

    public static String para5(Stockists dealer) {
        return "\nWhereas the Company is engaged in manufacturing of Solar Energy Products and having it’s manufacturing unit at K-240/241, M.I.D.C. Waluj, Aurangabad – 431 136.  Whereas the party at second part has requested to the party at first part to engage or appoint the party at second part as a Dealer of the party at first part, to sell and provide after sales service to the customers of the product of party at first part and therefore the party at first part has agreed to appoint the party at second part as their\n" +
                "Dealer for the Taluka " + dealer.getAddress().getTaluk() + " District " + dealer.getAddress().getDistrict() + " in the State of " + dealer.getAddress().getState() + " on the following terms and conditions.\n";
    }

    public static String para6() {
        StringBuilder para = new StringBuilder();
        para.append("\n1. The Dealer is authorized to sell the products offered by the company.  The Dealer (or it’s representative) can communicate various technical and commercial information regarding the products of the Company to their prospective customers based on training and various literature supplied by the Company from time to time. Company will not be responsible for communication of wrong information & false commitments made or given by the Dealer or its representatives to customers/end user.\n\n");
        para.append("\n2. Company will decide operating area for the Dealer and Dealer will have to restrict their operations within the area allocated to him only.\n\n");
        para.append("\n3. The Dealer have to fulfill all legal formalities (with amendments from time to time) with the Company such as execution of Agreement, Security Deposit payment etc. if required.\n\n");
        para.append("\n4. The Company will issue various information, circulars, letters, covering marketing strategy, price list, commission structure, incentive schemes, payment terms, etc. to “Dealer” from time to time. The same will be binding on Dealer.\n\n");
        para.append("\n5. However, Company reserves the right to alter or change the financial terms and conditions, prices, commission structure etc. without prior notice to the Dealer.\n\n");
        para.append("\n6. The Dealer will have to develop all necessary infrastructure for marketing, installations, and servicing of various products of Company as and when required.\n\n");
        para.append("\n7. The Dealer will provide timely services for Sudarshan Saur's products to the customer as per the servicing and maintenance guidelines provided by the company.\n\n");
        para.append("\n8. The Company products may be sold to user via two different “Sales Modes” depending on the profile of customer. The brief details are as under: -\n\n");
        para.append("\na) Dealer Sell Mode: - Wherein Company will raise the Invoices in the name of Dealer, and Dealer will sell the products to customer by raising his own Invoice.\n\n");
        para.append("\nb) Customer Sell Mode: - Wherein company will raise the Invoice directly in the name of customer and Dealer will be paid the mutually decided sells commission after completion of project & after full recovery of payment from customer.\n\n");
        para.append("\nHowever, in both the above cases full recovery of payment from customer and complete payment to Company against various invoices raised by Company i.e. (in case of dealer sell mode) & invoices raised to the customer (in case of customer sell mode) will be the sole responsibility of Dealer only.\n\n");
        para.append("\nAny un-recovered amount from any customer against Company invoices for any reason will be debited fromdealer’s account.\n\n");
        para.append("\nCompany will charge the interest to Dealer on delayed payment recoveries as per the terms and conditions decided by the Company from time to time and the same will be binding on Dealer.  \n\n");
        para.append("\n9. Dealer will have to furnish full details of customers like Permanent address where system is installed, Permanent Mobile no., Date of Installation, Photograph of the system with name plate, system capacity, Price of product, Google map location[ Geographical location of the swhs]etc. as per the formats furnished by Company from time to time.\n\n");
        para.append("\n10. The Company shall issue the account statement to the Dealer as & when required.  The Dealer has to acknowledge the same. If the dealer does not acknowledge the same within the period of 15 days from the date of issue, it shall be deemed by default that the dealer has acknowledged and accepted the account statement.\n\n");
        para.append("\n11. The Company, or their representative have full right to audit anytime all the working pattern of Dealer, which may cover marketing, installation, servicing & financial aspects.  The Dealer will provide free access to all such information to Company officials and extend full co. operation and support related to the third-party audits of installed systems during survey.\n\n");
        para.append("\n12. It is strictly understood by both/all the parties or in between the customers & both/ all the parties that the Jurisdiction of all the disputes (Civil/Criminal or otherwise) which may arise in between Company and customer or between company and any ofthe parties, shall be at Aurangabad where the Company is having its registered office.\n\n");
        para.append("\n13. Dealer will convey / inform to sub dealers/ salespersonall terms and conditions of the company related to, but not limited to, the giving service to the customer, and following all instruction given in user manual or in various circulars issued by company time to time.\n\n");
        para.append("\n14. Company also reserves right to terminate this Agreement without assigning any reason.\n\n\n\n\n");
        return para.toString();
    }

    public static String para7(Stockists stockists) {
        Date createdAt = stockists.getCreatedOn();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        return "Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + "                           Place: "+stockists.getAddress().getTown();
    }

    private static String getFullAddress(Address address) {
        return address.getHouseNo() + ", " + address.getArea() + ", " + address.getStreet1() + ", " + address.getStreet2() + ", " + address.getLandmark() + ", " + address.getTaluk() + ", " + address.getDistrict() + ", " + address.getTown() + ", " + address.getState() + " - " + address.getZipCode();
    }
}
