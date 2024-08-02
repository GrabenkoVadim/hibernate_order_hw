package mate.academy;

import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.*;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
    private static final MovieSessionService movieSessionService = (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static final CinemaHallService cinemaHallService = (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static final OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
    private static final ShoppingCartService shoppingCartService = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private static final AuthenticationService authenticationService = (AuthenticationService) injector.getInstance(AuthenticationService.class);

    public static void main(String[] args) throws RegistrationException {

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        User franko = authenticationService.register("frankoWarior@gmail.com", "mikky31");
        shoppingCartService.addSession(yesterdayMovieSession, franko);
        shoppingCartService.addSession(tomorrowMovieSession, franko);
        ShoppingCart frankosCart = shoppingCartService.getByUser(franko);
        orderService.completeOrder(frankosCart);
        for (Order order: orderService.getOrdersHistory(franko)) {
            System.out.println(order);
        }

        User stefan = authenticationService.register("stefanWarior@gmail.com", "mikky31");
        shoppingCartService.addSession(yesterdayMovieSession, stefan);
        shoppingCartService.addSession(tomorrowMovieSession, stefan);
        ShoppingCart stefansCart = shoppingCartService.getByUser(stefan);
        orderService.completeOrder(stefansCart);
        for (Order order: orderService.getOrdersHistory(stefan)) {
            System.out.println(order);
        }
    }
}
