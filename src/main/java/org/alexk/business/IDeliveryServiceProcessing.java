package org.alexk.business;

import org.alexk.business.models.MenuItem;
import org.alexk.business.models.Order;
import org.alexk.business.models.User;
import org.alexk.business.models.misc.FilterConfig;
import org.alexk.business.models.viewModels.MenuItemViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Interfata care abstractizeaza logica sistemului pentru serviciul de livrare de implementarea acestuia
 */
public interface IDeliveryServiceProcessing {
    /// ADMIN
    /**
     * Metoda care importa lista de produse in meniu
     * @param menuItems lista de produse care urmeaza sa fie importata
     * @pre menuItems != null
     * @post getMenuItems == menuItems
     */
    void importItems(List<MenuItem> menuItems);
    /**
     * Metoda care returneaza lista de produse din meniu
     * @return lista de produse din meniu
     * @pre true
     * @post nochange
     */
    List<MenuItem> getMenuItems();
    /**
     * Metoda care returneaza un produs cu id-ul specificat din meniu
     * @param id id-ul produsului care va fi cautat in meniu
     * @return produsul daca id se afla in meniu, null altfel
     * @pre id >= 0
     * @post nochange
     */
    MenuItem getMenuItem(int id);
    /**
     * Metoda care adauga in meniu un produs
     * @param item produsul ce va urma sa fie adaugat in meniu
     * @pre !exists x: getMenuItems() @ x.getTitle() == item.getTitle()
     * @post getMenuItems().size() == getMenuItems()@pre.size() + 1
     * @post forall k: [0..getMenuItems().size() - 1] @
     *  getMenuItems().indexOf(k) == getMenuItems()@pre.indexOf(k)
     */
    void addMenuItem(MenuItem item);
    /**
     * Metoda care actualizeaza un produs din meniu
     * @param id id-ul produsului care va urma sa fie actualizat
     * @param item noile valori ale produsului ce va urma sa fie actualizat
     * @pre exists x: getMenuItems() @ x.getId() == id
     * @pre !exists x: getMenuItems() @ x.getTitle() == item.getTitle()
     * @post getMenuItem(id) == item
     */
    void updateMenuItem(int id, MenuItem item);
    /**
     * Metoda care sterge un produs din meniu
     * @param id id-ul produsului care va urma sa fie sters
     * @pre exists x: getMenuItems() @ x.getId() == item.getId()
     * @post getMenuItems().size() == getMenuItems()@pre.size() - 1
     * @post getMenuItem(id) == null
     */
    void removeMenuItem(int id);
    /**
     * Metoda care verifica daca exista un produs cu titlul specificat
     * @param title titlul dupa care se face filtrarea
     * @return true daca exista deja un produs cu titlul specificat, false altfel
     * @pre !title.isBlank and !title.isEmpty()
     * @post nochange
     */
    boolean containsProduct(String title);
    /**
     * Metoda care genereaza un raport cu toate comenzile plasate intre orele specificate
     * @param startHour ora minima dupa care se face filtrarea
     * @param endHour ora maxima dupa care se face filtrarea
     * @pre startHour != null and endHour != null
     * @pre startHour.isBefore(endHour)
     * @post nochange
     */
    void generateReportBetweenHours(LocalTime startHour, LocalTime endHour);
    /**
     * Metoda care genereaza un raport cu toate produsele comandate de cel putin un numar specificat de ori
     * @param times numarul minim dupa care se face filtrarea
     * @pre times >= 0
     * @post nochange
     */
    void generateReportProducts(int times);
    /**
     * Metoda care genereaza un raport cu toate comenzile care sunt de o valoare minima si sunt plasate de clienti cu cel putin un numar speficat de comenzi
     * @param minNoOrders numarul minim de comenzi dupa care se face filtrarea
     * @param minSum valoarea minima dupa care se face filtrarea
     * @pre minNoOrders >= 0 and minSum >= 0
     * @post nochange
     */
    void generateReportClientSum(int minNoOrders, double minSum);
    /**
     * Metoda care genereaza un raport cu toate produsele comandate intr-o zi specificata
     * @param date data dupa care se face filtrarea
     * @pre date != null
     * @post nochange
     */
    void generateReportProductsInDay(LocalDate date);

    /// CUSTOMER
    /**
     * Metoda care filtreaza produsele din meniu dupa un filtru
     * @param config filtrul dupa care se face filtrarea
     * @return lista de produse filtrate
     * @pre config != null
     * @post nochange
     */
    List<MenuItem> filterMenuItems(FilterConfig config);
    /**
     * Metoda care creeaza o noua comanda
     * @param client clientul care a facut comanda
     * @param items produsele din comanda
     * @pre client != null and checkAccount(client) != null
     * @pre items != null and items.size() > 0
     * @post getOrders().size() == getOrders()@pre.size() + 1
     */
    void createOrder(User client, List<MenuItemViewModel> items);

    /// EMPLOYEE
    /**
     * Metoda care returneaza lista de comenzi
     * @return lista de comenzi
     * @pre true
     * @post nochange
     */
    List<Order> getOrders();

    /**
     * Metoda care verifica daca un cont de utilizator este valid
     * @param user datele care se vor verifica
     * @return datele gasite despre utilizator, null daca datele sunt eronate
     * @pre user != null
     * @post nochange
     */
    User checkAccount(User user);

    /**
     * Metoda care verifica daca invariantul a fost respectat
     * @return true daca invariantul inca este adevarat, false altfel
     */
    boolean isWellFormed();
}
