package net.myacxy.agsm.utils;

public class SharedItems
{
    public static class Action
    {
        public static class Server
        {
            public static final String ON_SERVER_ADDED = "net.myacxy.agsm.action.ON_SERVER_ADDED";
            public static final String ON_SERVER_REMOVED = "net.myacxy.agsm.action.ON_SERVER_REMOVED";

            public static final String ON_UPDATE_SERVER = "net.myacxy.agsm.action.ON_UPDATE_SERVER";
            public static final String ON_SERVER_UPDATED = "net.myacxy.agsm.action.ON_SERVER_UPDATED";

            public static final String UPDATE_SERVERS = "net.myacxy.agsm.action.UPDATE_SERVERS";
            public static final String ON_UPDATE_SERVERS = "net.myacxy.agsm.action.ON_UPDATE_SERVERS";
            public static final String ON_SERVERS_UPDATED = "net.myacxy.agsm.action.ON_SERVERS_UPDATED";
        }

        public static class Service
        {
            public static final String ENSURE_PERIODIC_REFRESH = "net.myacxy.agsm.action.ENSURE_PERIODIC_REFRESH";
            public static final String PERIODIC_REFRESH = "net.myacxy.agsm.action.PERIODIC_REFRESH";
        }
    }

    public static class UpdateReason
    {
        public static final int MANUAL = 0;
        public static final int PERIODIC = 1;
    }

    public static class Identifier
    {
        public static final int HOME = -10;
        public static final int NOTIFICATIONS = -11;
        public static final int SETTINGS = -12;
        public static final int ADD_SERVER = -13;

    }

    public static class Extra
    {
        public static final String UPDATE_REASON = "net.myacxy.agsm.extra.UPDATE_REASON";
        public static final String GAME_SERVER_ID = "net.myacxy.agsm.extra.GAME_SERVER_ID";
    }
}
