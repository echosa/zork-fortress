(ns zork-fortress.inventory)

(defn get-player-inventory
  "Returns the player's inventory."
  [game]
  (-> game :player :inventory))

(defn get-inventory-logs
  "Returns the logs vector from the player inventory."
  [game]
  (:logs (get-player-inventory game)))

(defn get-inventory-log-type-index
  "Returns the index of the log type in the inventory."
  [game log-type]
  (let [logs-in-inventory (get-inventory-logs game)]
    (let [log-inventory-entry (or (first (filterv #(= (:type %) log-type) logs-in-inventory)) {})]
      (.indexOf logs-in-inventory log-inventory-entry))))

(defn get-inventory-log-count
  "Returns the number of logs for the given type in the inventory."
  [game log-type]
  (let [log-count (:count (nth (get-inventory-logs game)
                               (get-inventory-log-type-index game log-type)))]
    log-count))

(defn update-log-inventory
  "Updates the given inventory log index by adding the given number of logs."
  [game index logs-to-add]
  (update-in game [:player :inventory :logs index :count]
                     (fn [a b]
                       (+ a b))
                     logs-to-add))

(defn add-logs-to-inventory
  "Add logs to the player inventory."
  [game tree]
  (let [game (if (contains? (-> game :player :inventory) :logs)
               game
               (assoc-in game [:player :inventory] {:logs []}))
        log-count (:log-count tree)
        log-type-index (get-inventory-log-type-index game (:type tree))]
    (if (= log-type-index -1)
      (let [current-logs (get-inventory-logs game)]
          (assoc-in game [:player :inventory :logs]
                        (conj current-logs
                              {:type (:type tree) :count log-count})))
      (update-log-inventory game log-type-index log-count))))

