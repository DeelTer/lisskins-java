package ru.deelter.lisskins.constants;

import lombok.experimental.UtilityClass;

/**
 * Constants for working with the LIS-SKINS API.
 */
@UtilityClass
public final class ApiConstants {

	@UtilityClass
	public static final class Game {

		/**
		 * Counter-Strike 2.
		 */
		public static final String CSGO = "csgo";

		/**
		 * Dota 2.
		 */
		public static final String DOTA2 = "dota2";

		/**
		 * Rust.
		 */
		public static final String RUST = "rust";
	}

	@UtilityClass
	public static final class SortBy {

		/**
		 * Sort by oldest first.
		 */
		public static final String OLDEST = "oldest";

		/**
		 * Sort by newest first.
		 */
		public static final String NEWEST = "newest";

		/**
		 * Sort by lowest price first.
		 */
		public static final String LOWEST_PRICE = "lowest_price";

		/**
		 * Sort by highest price first.
		 */
		public static final String HIGHEST_PRICE = "highest_price";
	}

	@UtilityClass
	public static final class PurchaseStatus {

		/**
		 * Waiting for trade lock to expire.
		 */
		public static final String WAIT_UNLOCK = "wait_unlock";

		/**
		 * Waiting for user to accept the trade offer.
		 */
		public static final String WAIT_ACCEPT = "wait_accept";

		/**
		 * Trade offer accepted.
		 */
		public static final String ACCEPTED = "accepted";

		/**
		 * Skin returned.
		 */
		public static final String RETURN = "return";

		/**
		 * Processing the purchase.
		 */
		public static final String PROCESSING = "processing";

		/**
		 * Waiting for withdrawal.
		 */
		public static final String WAIT_WITHDRAW = "wait_withdraw";
	}

	@UtilityClass
	public static final class ReturnReason {

		/**
		 * Withdrawal timeout exceeded.
		 */
		public static final String WAIT_WITHDRAW_TIMEOUT = "wait_withdraw_timeout";

		/**
		 * Manually cancelled.
		 */
		public static final String MANUAL_CANCEL = "manual_cancel";

		/**
		 * Trade offer timeout.
		 */
		public static final String TRADE_TIMEOUT = "trade_timeout";

		/**
		 * Error creating trade offer.
		 */
		public static final String TRADE_CREATE_ERROR = "trade_create_error";

		/**
		 * Trade offer cancelled.
		 */
		public static final String TRADE_CANCELED = "trade_canceled";

		/**
		 * Rollback initiated by user.
		 */
		public static final String ROLLBACK_USER = "rollback_user";

		/**
		 * Rollback initiated by supplier.
		 */
		public static final String ROLLBACK_SUPPLIER = "rollback_supplier";
	}

	@UtilityClass
	public static final class ErrorCode {

		/**
		 * Unknown error occurred.
		 */
		public static final String UNKNOWN_ERROR = "unknown_error";

		/**
		 * Invalid trade URL provided.
		 */
		public static final String INVALID_TRADE_URL = "invalid_trade_url";

		/**
		 * User cannot trade.
		 */
		public static final String USER_CANT_TRADE = "user_cant_trade";

		/**
		 * User inventory is private.
		 */
		public static final String PRIVATE_INVENTORY = "private_inventory";

		/**
		 * User has a trade ban.
		 */
		public static final String USER_TRADE_BAN = "user_trade_ban";

		/**
		 * User inventory is full.
		 */
		public static final String USER_INVENTORY_FULL = "user_inventory_full";
	}

	@UtilityClass
	public static final class WebSocketEvent {

		/**
		 * A new skin was added to the market.
		 */
		public static final String OBTAINED_SKIN_ADDED = "obtained_skin_added";

		/**
		 * A skin was removed from the market.
		 */
		public static final String OBTAINED_SKIN_DELETED = "obtained_skin_deleted";

		/**
		 * The price of a skin on the market changed.
		 */
		public static final String OBTAINED_SKIN_PRICE_CHANGED = "obtained_skin_price_changed";

		/**
		 * Purchase skin information was updated.
		 */
		public static final String PURCHASE_SKIN_INFO_UPDATED = "purchase_skin_info_updated";
	}
}