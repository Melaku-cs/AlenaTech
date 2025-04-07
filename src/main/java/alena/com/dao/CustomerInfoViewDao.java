package alena.com.dao;
import alena.com.models.SaccoAccount;
import alena.com.models.CustomerInfoView;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
@Stateless
public class CustomerInfoViewDao {
    @PersistenceContext(unitName = "primary")
    private EntityManager em;
    @PersistenceContext(unitName = "oracle")
    private EntityManager em2;
    public List<CustomerInfoView> getAccounts(final String phone) {
        final TypedQuery<CustomerInfoView> accounts =
                (TypedQuery<CustomerInfoView>) this.em.createQuery("SELECT DISTINCT c FROM CustomerInfoView c where c.phone=:phone", (Class) CustomerInfoView.class).setParameter("phone", (Object) phone);
        return accounts.getResultList();
    }
    public CustomerInfoView getBranch(final String account) {
        final TypedQuery<CustomerInfoView> accounts = (TypedQuery<CustomerInfoView>)this.em.createQuery("SELECT DISTINCT c FROM CustomerInfoView c where c.account=:account", (Class)CustomerInfoView.class).setParameter("account", (Object)account);
        return accounts.getSingleResult();
    }
    public List<String> getAllTenantIds() {
        try {
            TypedQuery<String> query = em2.createQuery(
                    "SELECT s.tenantId FROM SaccoAccount s", String.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public List<String> getTenantIdsByAccount(String account) {
        try {
            // Create a parameterized query with OR condition
            TypedQuery<String> query = em2.createQuery(
                    "SELECT s.tenantId FROM SaccoAccount s WHERE s.account = :account "
//                            +
//                            "OR s.saccoAccountNumber = :saccoAccountNumber"
                    ,
                    String.class
            );
            // Set query parameters
            query.setParameter("account", account);
//            query.setParameter("saccoAccountNumber", saccoAccountNumber);

            // Execute the query and return the result list
            return query.getResultList();
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            // Return an empty list in case of any error
            return new ArrayList<>();
        }
    }


    //    public String getTenantIdByAccount(final String account) {
//        // Construct the query to retrieve Tenant ID from the SaccoAccount table
//        TypedQuery<SaccoAccount> query = this.em.createQuery(
//                "SELECT s FROM SaccoAccount s WHERE s.account = :account",
//                SaccoAccount.class
//        ).setParameter("account", account);
//
//        // Get the result (Single result since account is unique)
//        SaccoAccount saccoAccount = query.getSingleResult();
//
//        // Return the Tenant ID
//        return saccoAccount != null ? saccoAccount.getTenantId() : null;
//    }
public String getWegaSettlementAcc(String tenantId) {
        System.out.println("Inside getWegaSettlementAcc tenantId"+tenantId);
    try {
        // Use a TypedQuery to fetch a single value from the database
        return em2.createQuery(
                        "SELECT c.account FROM SaccoAccount c WHERE c.tenantId = :tenantId", String.class)
                .setParameter("tenantId", tenantId)
                .getSingleResult();
    } catch (NoResultException e) {
        System.out.println("No account found for tenantId: " + tenantId);
        return null;
    }
}}



