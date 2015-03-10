(ns zork-fortress.game
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-area-from-game [t2/Game t/Int -> t2/Area])
(defn get-area-from-game
  "Returns the area in the game with the given ID."
  [game area-id]
  {:pre [(seq (:areas (:world game)))]}
  (nth (filterv (fn [item] (= (:id item) area-id))
                (:areas (:world game)))
       0))

(t/ann get-current-area [t2/Game -> t2/Area])
(defn get-current-area
  "Returns the current area the player is in."
  [game]
  (get-area-from-game game (:current-area game)))

(t/ann tree-count-reduction [(t/Map String t/Int) t2/TreeTypeColl -> (t/Map String t/Int)])
(defn- tree-count-reduction
  "Used as part of (get-current-area-tree-counts)."
  [val next]
  (merge val {(:type next) (count (:trees next))}))

(t/ann get-current-area-tree-counts [t2/Game -> (t/Map String t/Int)])
(defn get-current-area-tree-counts
  "Return a map of maps with tree counts for the current area."
   [game]
   (let [trees (:trees (get-current-area game))]
     (reduce tree-count-reduction
             {}
             trees)))

(t/ann get-tree-type-from-area [t2/Game t/Int String -> t2/TreeTypeColl])
(defn get-tree-type-from-area
  "Return the map for the given tree type from the given area."
  [game area-id tree-type]
  (let [area (get-area-from-game game area-id)
        trees (:trees area)
        filtered-tree-type (filterv (fn [item] (= (:type item) tree-type))
                                    trees)]
    (if (and (not (nil? filtered-tree-type)) (not (empty? filtered-tree-type)))
     (nth filtered-tree-type 0)
     {:type tree-type :trees []})))

(t/ann get-tree-types-from-area-without-type [t2/Game t/Int String -> (t/U nil (t/Vec t2/TreeTypeColl))])
(defn get-tree-types-from-area-without-type
  "Return the vector of tress for the given area without the given tree type."
  [game area-id tree-type]
  (let [area (get-area-from-game game area-id)
        trees (:trees area)
        tree-types (filterv (fn [item] (not= (:type item) tree-type))
                            trees)]
    (when (and (not (nil? tree-type)) (not (empty? tree-type)))
     tree-types)))

(t/ann get-next-tree-id-to-add [t2/Game t/Int String -> t/Int])
(defn get-next-tree-id-to-add
  "Returns the next tree id to be added to the given area."
  [game area-id tree-type]
  (let [trees (:trees (get-tree-type-from-area game area-id tree-type))]
    (if (empty? trees)
      1
      (+ 1 (:id (last trees))))))

(t/ann add-tree-to-area [t2/Game t/Int String t/Int -> t2/Game])
(defn add-tree-to-area
  "Returns the game with a tree added to the given area."
  [game area-id tree-type log-count]
  (let [next-id (get-next-tree-id-to-add game area-id tree-type)
        new-tree {:id next-id :type tree-type :log-count log-count}
        existing-area (get-area-from-game game area-id)
        existing-tree-map (get-tree-type-from-area game area-id tree-type)
        existing-trees (:trees existing-tree-map)
        updated-trees (conj existing-trees new-tree)
        updated-tree-map {:type tree-type :trees updated-trees}
        updated-tree-vector [updated-tree-map]
        updated-area {:id (:id existing-area)
                      :name (:name existing-area)
                      :type (:type existing-area)
                      :trees (conj (or (get-tree-types-from-area-without-type game area-id tree-type) [])
                                   updated-tree-map)}
        updated-world (merge (:world game) {:areas [updated-area]})]
    (merge game {:world updated-world})))

(t/ann is-treetypecoll-of-type [t2/TreeTypeColl String -> t/Bool])
(defn is-treetypecoll-of-type
  "Returns true if the type of the given TreeTypeColl is the given type."
  [tree-type-coll tree-type]
  (= (:type tree-type-coll) tree-type))

(t/ann filter-trees-of-type [(t/Vec t2/TreeTypeColl) String -> (t/Vec t2/TreeTypeColl)])
(defn filter-trees-of-type
  "Returns a collection of TreeTypeColl matching the given type."
  [trees tree-type]
  (filterv (t/fn [tree-type-coll :- t2/TreeTypeColl]
             (is-treetypecoll-of-type tree-type-coll tree-type)
             ;;(get-tree-type-from-treetypecoll tree-type-coll tree-type)
             )
           trees))

(t/ann get-trees-with-type-from-area [t2/Area String -> (t/Option (t/Vec t2/Tree))])
(defn get-trees-with-type-from-area
  "Returns the trees with the give type from the given area."
  [area tree-type]
  (let [trees (:trees area)]
    (when-not (nil? trees)
      (let [first-filtered-tree-coll (first (filter-trees-of-type trees tree-type))]
        (when-not (nil? first-filtered-tree-coll)
          (:trees first-filtered-tree-coll))))))

(t/ann tree-has-id [t2/Tree t/Int -> t/Bool])
(defn tree-has-id
  "Returns true if the tree has the given id."
  [tree id]
  (= (:id tree) id))

(t/ann filter-trees-with-id [(t/Vec t2/Tree) t/Int -> (t/Vec t2/Tree)])
(defn filter-trees-with-id
  "Returns trees that match the given id."
  [trees id]
  (filterv (t/fn [tree :- t2/Tree]
             (tree-has-id tree id))
           trees))

(t/ann area-has-tree-with-id [t2/Area String t/Int -> (t/Option t2/Tree)])
(defn area-has-tree-with-id
  "Returns the first tree with the given id if it exists in the area, nil otherwise."
  [area tree-type tree-id]
  (let [trees-of-type (get-trees-with-type-from-area area tree-type)]
    (when-not (nil? trees-of-type)
      (let [filtered-trees (filter-trees-with-id trees-of-type tree-id)]
        (first filtered-trees)))))

(t/ann get-next-tree-of-type [t2/Game t2/Area String -> (t/Option t2/Tree)])
(defn get-next-tree-of-type
  "Returns the next tree available for the given type, or nil if there are none."
  [game area tree-type]
  (let [area-tree-map (get-tree-type-from-area game (:id area) tree-type)]
    (first (:trees area-tree-map))))

(t/ann get-inventory-log-type-index [t2/Game String -> t/Int])
(defn get-inventory-log-type-index
  "Returns the index of the log type in the inventory."
  [game log-type]
  (let [logs-in-inventory (-> game :player :inventory :logs)]
    (assert logs-in-inventory)
    (let [log-inventory-entry (or (first (filterv #(= (:type %) log-type) logs-in-inventory)) {})]
      (assert (instance? clojure.lang.APersistentVector logs-in-inventory))
      (.indexOf ^clojure.lang.APersistentVector logs-in-inventory log-inventory-entry))))

(t/ann get-inventory-log-count [t2/Game String -> t/Int])
(defn get-inventory-log-count
  "Returns the number of logs for the given type in the inventory."
  [game log-type]
  (let [log-type-index (get-inventory-log-type-index game log-type)
        log-count (:count (nth (-> game :player :inventory :logs) log-type-index))]
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
      (let [current-logs (-> game :player :inventory :logs)
            _ (assert current-logs)]
          (t2/assoc-in* game [:player :inventory :logs]
                        (conj current-logs
                              {:type (:type tree) :count log-count})))
      (update-log-inventory game log-type-index log-count))))

(t/ann remove-tree-from-area [t2/Game t2/Area t2/Tree -> t2/Game])
(defn remove-tree-from-area
  "Returns the game with the given tree remove to the given area."
  [game area tree]
  (let [tree-type (:type tree)
        tree-id (:id tree)
        existing-tree-map (get-tree-type-from-area game (:id area) tree-type)
        existing-trees (:trees existing-tree-map)
        tree-to-remove (area-has-tree-with-id area tree-type tree-id)]
    (if (nil? tree-to-remove)
      game
      (let [updated-trees (filterv (fn [tree-to-check] (not= (:id tree-to-check) tree-id)) existing-trees)
            updated-tree-map {:type tree-type :trees updated-trees}
            updated-area {:id (:id area)
                          :name (:name area)
                          :type (:type area)
                          :trees (conj (or (get-tree-types-from-area-without-type game (:id area) tree-type) [])
                                       updated-tree-map)}
            updated-world (merge (:world game) {:areas [updated-area]})]
        (merge game {:world updated-world})))))
