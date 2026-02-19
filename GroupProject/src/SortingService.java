import java.util.List;

public interface SortingService {
    void sortByName(List<User> userList);
    void sortByPassword(List<User> userList);
    void sortByEmail(List<User> userList);
}
