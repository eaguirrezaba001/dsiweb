package eus.ehu.dsiweb.ekain.eltenedor;

import eus.ehu.dsiweb.entity.DBUser;

public class Session {

    private static Session session;
    private DBUser loggedUser;

    private Session(){
    }

    public static Session getInstance(){
        if(session==null){
            session = new Session();
        }
        return session;
    }

    public DBUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(DBUser loggedUser) {
        this.loggedUser = loggedUser;
    }

}
