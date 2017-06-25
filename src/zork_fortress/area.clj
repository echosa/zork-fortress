(ns zork-fortress.area
  (:require [zork-fortress.tree :as tree]
            [clojure.walk :as w]))

(defn get-area-from-game
  "Returns the area in the game with the given ID."
  [game area-id]
  {:pre [(seq (-> game :world :areas))]}
  (nth (filterv (fn [item] (= (:id item) area-id))
                (-> game :world :areas))
       0))

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

(defn get-tree-types-from-area-without-type
  "Return the vector of tress for the given area without the given tree type."
  [game area-id tree-type]
  (when (and (some? tree-type) (seq tree-type))
    (filterv (fn [item] (not= (:type item) tree-type))
             (:trees (get-area-from-game game area-id)))))

(defn get-next-tree-id-to-add
  "Returns the next tree id to be added to the given area."
  [game area-id tree-type]
  (let [trees (:trees (get-tree-type-from-area game area-id tree-type))]
    (if (empty? trees)
      1
      (+ 1 (:id (last trees))))))

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

(defn get-trees-with-type-from-area
  "Returns the trees with the give type from the given area."
  [area tree-type]
  (let [trees (:trees area)]
    (when (seq trees)
      (let [first-filtered-tree-coll (first (tree/filter-trees-of-type trees tree-type))]
        (when (some? first-filtered-tree-coll)
          (:trees first-filtered-tree-coll))))))

(defn area-has-tree-with-id
  "Returns the first tree with the given id if it exists in the area, nil otherwise."
  [area tree-type tree-id]
  (let [trees-of-type (get-trees-with-type-from-area area tree-type)]
    (when (seq trees-of-type)
      (first (tree/filter-trees-with-id trees-of-type tree-id)))))

(defn get-next-tree-of-type
  "Returns the next tree available for the given type, or nil if there are none."
  [game area tree-type]
  (first (:trees (get-tree-type-from-area game (:id area) tree-type))))

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

(defn get-tree-types
  "Returns a list of available tree types."
  [area]
  (w/walk :type conj (:trees area)))
