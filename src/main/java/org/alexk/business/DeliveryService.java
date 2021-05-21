package org.alexk.business;

import lombok.val;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.Order;
import org.alexk.business.models.OrderItem;
import org.alexk.business.models.User;
import org.alexk.business.models.enums.UserType;
import org.alexk.business.models.misc.FilterConfig;
import org.alexk.business.models.misc.SerializeData;
import org.alexk.business.models.viewModels.MenuItemViewModel;
import org.alexk.data.FileWriter;
import org.alexk.data.Serializator;
import org.alexk.utils.DataIntegrityChecker;
import org.alexk.utils.MenuItemFilter;
import org.alexk.utils.ReportGenerator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clasa care se ocupa de intretinerea sistemului pentru serviciul de livrare
 * @invariant isWellFormed()
 */
public class DeliveryService implements IDeliveryServiceProcessing {
    /**
     * Obiect folosit la implementarea Observer Design Pattern (Observable)
     */
    private PropertyChangeSupport pcs;
    /**
     * Obiect folosit la memorarea produselor din meniu
     */
    private List<MenuItem> menuItems;
    /**
     * Obiect folosit la memorarea comenzilor si a produselor corespunzatoare
     */
    private Map<Order, List<OrderItem>> orders;
    /**
     * Obiect folosit la memorarea datelor de logare a utilizatorilor
     */
    private List<User> accounts;
    /**
     * Obiect folosit la implementarea Singleton Design Pattern
     */
    public static final DeliveryService instance = new DeliveryService();

    /**
     * Creeaza si initializeaza singletonul
     */
    private DeliveryService() {
        init();
    }

    /// ADMIN
    /**
     * Metoda care importa lista de produse in meniu
     * @param menuItems lista de produse care urmeaza sa fie importata
     */
    public void importItems(List<MenuItem> menuItems) {
        assert menuItems != null;
        assert isWellFormed();
        this.menuItems = menuItems;
        System.out.println(MenuItem.nextId);
        assert getMenuItems() == menuItems;
        assert isWellFormed();
    }
    /**
     * Metoda care returneaza lista de produse din meniu
     * @return lista de produse din meniu
     */
    public List<MenuItem> getMenuItems() {
        assert true;
        assert isWellFormed();
        val copiedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val res = menuItems;
        assert new SerializeData(menuItems, orders, accounts).equals(copiedData);
        assert isWellFormed();
        return res;
    }
    /**
     * Metoda care returneaza un produs cu id-ul specificat din meniu
     * @param id id-ul produsului care va fi cautat in meniu
     * @return produsul daca id se afla in meniu, null altfel
     */
    public MenuItem getMenuItem(int id) {
        assert id >= 0;
        assert isWellFormed();
        val copiedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val result = menuItems.stream().filter(x -> x.getId() ==  id).findFirst().orElse(null);
        assert new SerializeData(menuItems, orders, accounts).equals(copiedData);
        assert isWellFormed();
        return result;
    }
    /**
     * Metoda care adauga in meniu un produs
     * @param item produsul ce va urma sa fie adaugat in meniu
     */
    public void addMenuItem(MenuItem item) {
        assert getMenuItems().stream().noneMatch(x -> x.getTitle().equals(item.getTitle()));
        assert isWellFormed();
        val copiedData = new ArrayList<>(menuItems);
        menuItems.add(item);
        for(int i = 0; i < getMenuItems().size() - 1; i++) {
            val k = getMenuItems().get(i);
            assert getMenuItems().indexOf(k) == copiedData.indexOf(k);
        }
        assert isWellFormed();
    }
    /**
     * Metoda care actualizeaza un produs din meniu
     * @param id id-ul produsului care va urma sa fie actualizat
     * @param item noile valori ale produsului ce va urma sa fie actualizat
     */
    public void updateMenuItem(int id, MenuItem item) {
        assert getMenuItems().stream().anyMatch(x -> x.getId() == id);
        assert getMenuItems().stream().noneMatch(x -> x.getTitle().equals(item.getTitle()));
        assert isWellFormed();
        removeMenuItem(id);
        addMenuItem(item);
        assert getMenuItem(id) == item;
        assert isWellFormed();
    }
    /**
     * Metoda care sterge un produs din meniu
     * @param id id-ul produsului care va urma sa fie sters
     */
    public void removeMenuItem(int id) {
        assert getMenuItems().stream().anyMatch(x -> x.getId() == id);
        assert isWellFormed();
        val preSize = getMenuItems().size();
        menuItems.removeIf(x -> x.getId() == id);
        assert getMenuItems().size() == preSize - 1;
        assert getMenuItem(id) == null;
        assert isWellFormed();
    }
    /**
     * Metoda care verifica daca exista un produs cu titlul specificat
     * @param title titlul dupa care se face filtrarea
     * @return true daca exista deja un produs cu titlul specificat, false altfel
     */
    public boolean containsProduct(String title) {
        assert !title.isBlank() && !title.isEmpty();
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val result = menuItems.stream().anyMatch(x -> x.getTitle().equals(title));
        assert isWellFormed();
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        return result;
    }
    /**
     * Metoda care genereaza un raport cu toate comenzile plasate intre orele specificate
     * @param startHour ora minima dupa care se face filtrarea
     * @param endHour ora maxima dupa care se face filtrarea
     */
    public void generateReportBetweenHours(LocalTime startHour, LocalTime endHour) {
        assert startHour != null && endHour != null;
        assert startHour.isBefore(endHour);
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val report = ReportGenerator.generateReportBetweenHours(getOrders(), startHour, endHour);
        FileWriter.saveReport("./reports/ReportBetweenHours.txt", report);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
    }
    /**
     * Metoda care genereaza un raport cu toate produsele comandate de cel putin un numar specificat de ori
     * @param times numarul minim dupa care se face filtrarea
     */
    public void generateReportProducts(int times) {
        assert times >= 0;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val report = ReportGenerator.generateReportProducts(menuItems, orders, times);
        FileWriter.saveReport("./reports/ReportProducts.txt", report);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
    }
    /**
     * Metoda care genereaza un raport cu toate comenzile care sunt de o valoare minima si sunt plasate de clienti cu cel putin un numar speficat de comenzi
     * @param minNoOrders numarul minim de comenzi dupa care se face filtrarea
     * @param minSum valoarea minima dupa care se face filtrarea
     */
    public void generateReportClientSum(int minNoOrders, double minSum) {
        assert minNoOrders >= 0 && minSum >= 0;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val report = ReportGenerator.generateReportClientSum(accounts, orders, minNoOrders, minSum);
        FileWriter.saveReport("./reports/ReportClientSum.txt", report);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
    }
    /**
     * Metoda care genereaza un raport cu toate produsele comandate intr-o zi specificata
     * @param date data dupa care se face filtrarea
     */
    public void generateReportProductsInDay(LocalDate date) {
        assert date != null;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val report = ReportGenerator.generateReportProductsInDay(menuItems, orders, date);
        FileWriter.saveReport("./reports/ReportProductsInDay.txt", report);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
    }

    /// CUSTOMER
    /**
     * Metoda care filtreaza produsele din meniu dupa un filtru
     * @param config filtrul dupa care se face filtrarea
     * @return lista de produse filtrate
     */
    public List<MenuItem> filterMenuItems(FilterConfig config) {
        assert config != null;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        var result = MenuItemFilter.filterMenuItems(menuItems, config);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
        return result;
    }
    /**
     * Metoda care creeaza o noua comanda
     * @param client clientul care a facut comanda
     * @param items produsele din comanda
     */
    public void createOrder(User client, List<MenuItemViewModel> items) {
        assert client != null && checkAccount(client) != null;
        assert items != null && items.size() > 0;
        assert isWellFormed();
        val preSize = getOrders().size();
        val id = orders.values().size();

        val date = new Date();
        val formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        val clientId = accounts.indexOf(client);
        val order = new Order(id, formatter.format(date), clientId);

        val orderItems = items.stream()
                .map(x -> new OrderItem(getMenuItem(x.getId()), x.getQuantity()))
                .collect(Collectors.toList());

        orders.put(order, orderItems);

        pcs.firePropertyChange("orders", null, order);
        FileWriter.generateBill(order);
        assert getOrders().size() == preSize + 1;
        assert isWellFormed();
    }

    /// EMPLOYEE
    /**
     * Metoda care returneaza lista de comenzi
     * @return lista de comenzi
     */
    public List<Order> getOrders() {
        assert true;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val result = new ArrayList<>(orders.keySet());
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
        return result;
    }
    /**
     * Metoda care returneaza lista produse din cadrul unei comenzi
     * @param id id-ul comenzii a carei lista de produse se vor returna
     * @return lista de produse
     * @throws Exception daca nu exista comanda cu id-ul specificat
     */
    public List<OrderItem> getOrderItems(int id) throws Exception {
        assert isWellFormed();
        val order = orders.keySet().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow();
        val result = getOrderItems(order);
        assert isWellFormed();
        return result;
    }
    /**
     * Metoda care returneaza lista produse din cadrul unei comenzi
     * @param order comanda a carei lista de produse se vor returna
     * @return lista de produse
     */
    public List<OrderItem> getOrderItems(Order order) {
        assert isWellFormed();
        val result = orders.get(order);
        assert isWellFormed();
        return result;
    }

    /// Observer stuff
    /**
     * Metoda care permite unui observer sa observer acest obiect
     * @param pcl observer-ul care urmeaza sa observe acest obiect
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        assert isWellFormed();
        pcs.addPropertyChangeListener(pcl);
        assert isWellFormed();
    }

    // Misc
    /**
     *  Metoda care face initializarea obiectului de tip singleton
     */
    private void init() {
        try {
            pcs = new PropertyChangeSupport(this);

            val data = Serializator.deserialize();
            menuItems = data.getMenuItems();
            orders = data.getOrders();
            accounts = data.getAccounts();
        } catch (Exception e) {
            System.out.println("Delivery Service:\n" + e.getMessage());
        }
    }
    /**
     * Metoda care salveaza datele aplicatiei intr-un fisier
     */
    public void save() {
        assert isWellFormed();
        try {
            val data = new SerializeData(menuItems, orders, accounts);
            Serializator.serialize(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert isWellFormed();
    }

    /**
     * Metoda care creeaza un nou cont de utilizator
     * @param user datele cu care se inregistreaza utilizatorul
     * @return datele despre utilizatorul
     */
    public User createAccount(User user) {
        assert isWellFormed();
        user.setType(UserType.CUSTOMER);
        accounts.add(user);
        assert isWellFormed();
        return user;
    }
    /**
     * Metoda care verifica daca un cont de utilizator este valid
     * @param user datele care se vor verifica
     * @return datele gasite despre utilizator, null daca datele sunt eronate
     */
    public User checkAccount(User user) {
        assert user != null;
        assert isWellFormed();
        val clonedData = SerializeData.getClonedData(menuItems, orders, accounts);
        val result = accounts.stream()
                .filter(x ->    x.getUsername().equals(user.getUsername()) &&
                                x.getPassword().equals(user.getPassword()))
                .findFirst().orElse(null);
        assert new SerializeData(menuItems, orders, accounts).equals(clonedData);
        assert isWellFormed();
        return result;
    }
    /**
     * Metoda care verifica daca un nume de utilizator este disponibil
     * @param username numele de utilizator care va fi verificat
     * @return true daca nu exista deja, false altfel
     */
    public boolean checkUsername(String username) {
        assert isWellFormed();
        val result = accounts.stream().noneMatch(x -> x.getUsername().equals(username));
        assert isWellFormed();
        return result;
    }

    /**
     * Metoda care verifica daca invariantul a fost respectat
     * @return true daca invariantul inca este adevarat, false altfel
     */
    @Override
    public boolean isWellFormed() {
        return DataIntegrityChecker.isWellFormed(menuItems, orders, accounts);
    }
}
