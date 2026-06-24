public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public String findCustomerById(int id) {
        return repository.findCustomerById(id);
    }
}
