(ns zork-fortress.inventory
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.player :as p]))

(t/ann get-inventory-logs [t2/Game -> (t/Option t2/InventoryLogs)])
(defn get-inventory-logs
  "Returns the logs vector from the player inventory."
  [game]
  (:logs (p/get-player-inventory game)))

(t/ann get-inventory-log-type-index [t2/Game String -> t/Int])
(defn get-inventory-log-type-index
  "Returns the index of the log type in the inventory."
  [game log-type]
  (let [logs-in-inventory (get-inventory-logs game)]
    (assert logs-in-inventory)
    (let [log-inventory-entry (or (first (filterv #(= (:type %) log-type) logs-in-inventory)) {})]
      (assert (instance? clojure.lang.APersistentVector logs-in-inventory))
      (.indexOf ^clojure.lang.APersistentVector logs-in-inventory log-inventory-entry))))

(t/ann get-inventory-log-count [t2/Game String -> t/Int])
(defn get-inventory-log-count
  "Returns the number of logs for the given type in the inventory."
  [game log-type]
  (let [log-count (:count (nth (get-inventory-logs game)
                               (get-inventory-log-type-index game log-type)))]
    (assert (instance? Number log-count))
    log-count))

(t/ann ^:no-check update-log-inventory [t2/Game t/Int t/Int -> t2/Game])
(defn update-log-inventory
  "Updates the given inventory log index by adding the given number of logs."
  [game index logs-to-add]
  (t2/update-in* game [:player :inventory :logs index :count]
                     (t/fn [a :- (t/Option t/Int)
                            b :- (t/Option t/Int)]
                       {:pre [a b]}
                       (+ a b))
                     logs-to-add))

(t/ann add-logs-to-inventory [t2/Game t2/Tree -> t2/Game])
(defn add-logs-to-inventory
  "Add logs to the player inventory."
  [game tree]
  (let [game (if (contains? (-> game :player :inventory) :logs)
               game
               (t2/assoc-in* game [:player :inventory] {:logs []}))
        log-count (:log-count tree)
        log-type-index (get-inventory-log-type-index game (:type tree))]
    (if (= log-type-index -1)
      (let [current-logs (get-inventory-logs game)
            _ (assert current-logs)]
          (t2/assoc-in* game [:player :inventory :logs]
                        (conj current-logs
                              {:type (:type tree) :count log-count})))
      (update-log-inventory game log-type-index log-count))))

