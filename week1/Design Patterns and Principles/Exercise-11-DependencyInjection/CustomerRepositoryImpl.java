public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public String findCustomerById(int id) {
        
        if (id == 101) {
            return "John Doe";
        } else if (id == 102) {
            return "Jane Smith";
        } else if (id == 103) {
            return "Alice Johnson";
        }
        return null; 
    }
}
