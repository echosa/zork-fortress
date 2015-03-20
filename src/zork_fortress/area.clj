(ns zork-fortress.area
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.tree :as tree]))

(t/ann get-area-from-game [t2/Game t/Int -> t2/Area])
(defn get-area-from-game
  "Returns the area in the game with the given ID."
  [game area-id]
  {:pre [(seq (-> game :world :areas))]}
  (nth (filterv (fn [item] (= (:id item) area-id))
                (-> game :world :areas))
       0))

(t/ann get-tree-type-from-area [t2/Game t/Int String -> t2/TreeTypeGroup])
(defn get-tree-type-from-area
  "Return the map for the given tree type from the given area."
  [game area-id tree-type]
  (let [area (get-area-from-game game area-id)
        trees (:trees area)
        filtered-tree-type (filterv (fn [item] (= (:type item) tree-type))
                                    trees)]
    (if (and (some? filtered-tree-type) (seq filtered-tree-type))
     (nth filtered-tree-type 0)
     {:type tree-type :trees []})))

(t/ann get-tree-types-from-area-without-type [t2/Game t/Int String -> (t/U nil (t/Vec t2/TreeTypeGroup))])
(defn get-tree-types-from-area-without-type
  "Return the vector of tress for the given area without the given tree type."
  [game area-id tree-type]
  (when (and (some? tree-type) (seq tree-type))
    (filterv (fn [item] (not= (:type item) tree-type))
             (:trees (get-area-from-game game area-id)))))

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

(t/ann get-trees-with-type-from-area [t2/Area String -> (t/Option (t/Vec t2/Tree))])
(defn get-trees-with-type-from-area
  "Returns the trees with the give type from the given area."
  [area tree-type]
  (let [trees (:trees area)]
    (when (seq trees)
      (let [first-filtered-tree-coll (first (tree/filter-trees-of-type trees tree-type))]
        (when (some? first-filtered-tree-coll)
          (:trees first-filtered-tree-coll))))))

(t/ann area-has-tree-with-id [t2/Area String t/Int -> (t/Option t2/Tree)])
(defn area-has-tree-with-id
  "Returns the first tree with the given id if it exists in the area, nil otherwise."
  [area tree-type tree-id]
  (let [trees-of-type (get-trees-with-type-from-area area tree-type)]
    (when (seq trees-of-type)
      (first (tree/filter-trees-with-id trees-of-type tree-id)))))

(t/ann get-next-tree-of-type [t2/Game t2/Area String -> (t/Option t2/Tree)])
(defn get-next-tree-of-type
  "Returns the next tree available for the given type, or nil if there are none."
  [game area tree-type]
  (first (:trees (get-tree-type-from-area game (:id area) tree-type))))

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
