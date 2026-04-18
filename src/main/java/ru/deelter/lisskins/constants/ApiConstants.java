package ru.deelter.lisskins.constants;

/**
 * Константы для работы с LIS-SKINS API.
 */
public final class ApiConstants {

    private ApiConstants() {}
    
    public static final class Game {
        public static final String CSGO = "csgo";
        public static final String DOTA2 = "dota2";
        public static final String RUST = "rust";
        private Game() {}
    }

    public static final class SortBy {
        public static final String OLDEST = "oldest";
        public static final String NEWEST = "newest";
        public static final String LOWEST_PRICE = "lowest_price";
        public static final String HIGHEST_PRICE = "highest_price";
        private SortBy() {}
    }

    public static final class PurchaseStatus {
        public static final String WAIT_UNLOCK = "wait_unlock";
        public static final String WAIT_ACCEPT = "wait_accept";
        public static final String ACCEPTED = "accepted";
        public static final String RETURN = "return";
        public static final String PROCESSING = "processing";
        public static final String WAIT_WITHDRAW = "wait_withdraw";
        private PurchaseStatus() {}
    }

    public static final class ReturnReason {
        public static final String WAIT_WITHDRAW_TIMEOUT = "wait_withdraw_timeout";
        public static final String MANUAL_CANCEL = "manual_cancel";
        public static final String TRADE_TIMEOUT = "trade_timeout";
        public static final String TRADE_CREATE_ERROR = "trade_create_error";
        public static final String TRADE_CANCELED = "trade_canceled";
        public static final String ROLLBACK_USER = "rollback_user";
        public static final String ROLLBACK_SUPPLIER = "rollback_supplier";
        private ReturnReason() {}
    }

    public static final class ErrorCode {
        public static final String UNKNOWN_ERROR = "unknown_error";
        public static final String INVALID_TRADE_URL = "invalid_trade_url";
        public static final String USER_CANT_TRADE = "user_cant_trade";
        public static final String PRIVATE_INVENTORY = "private_inventory";
        public static final String USER_TRADE_BAN = "user_trade_ban";
        public static final String USER_INVENTORY_FULL = "user_inventory_full";
        private ErrorCode() {}
    }

    public static final class WebSocketEvent {
        public static final String OBTAINED_SKIN_ADDED = "obtained_skin_added";
        public static final String OBTAINED_SKIN_DELETED = "obtained_skin_deleted";
        public static final String OBTAINED_SKIN_PRICE_CHANGED = "obtained_skin_price_changed";
        public static final String PURCHASE_SKIN_INFO_UPDATED = "purchase_skin_info_updated";
        private WebSocketEvent() {}
    }
}