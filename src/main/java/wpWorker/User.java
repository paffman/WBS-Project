package wpWorker;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Klasse fuer eingeloggte User<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 22.07.2012
 */
public class User extends Worker {
    public User(String login, boolean leiter) {
        super(login, leiter);
    }

    public User(String login, int id, String name, String vorname,
            boolean leiter) {
        super(login, id, name, vorname, (leiter) ? 1 : 0, 0);
    }
}
