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

(t/ann get-next-tree-id [t2/Game t/Int String -> t/Int])
(defn get-next-tree-id
  "Returns the next tree id for the given area."
  [game area-id tree-type]
  (let [trees (:trees (get-tree-type-from-area game area-id tree-type))]
    (if (empty? trees)
      1
      (+ 1 (:id (last trees))))))

(t/ann add-tree-to-area [t2/Game t/Int String t/Int -> t2/Game])
(defn add-tree-to-area
  "Returns the game with a tree added to the given area."
  [game area-id tree-type log-count]
  (let [next-id (get-next-tree-id game area-id tree-type)
        new-tree {:id next-id :type tree-type :log-count log-count}
        existing-area (get-area-from-game game area-id)
        existing-tree-map (get-tree-type-from-area game area-id tree-type)
        existing-trees (:trees existing-tree-map)
        updated-trees (conj existing-trees new-tree)
        updated-tree-map {:type tree-type :trees updated-trees}
        updated-tree-vector [updated-tree-map]
        updated-area {:id 1
                      :name "First Area" 
                      :type "plains"
                      :trees (conj (or (get-tree-types-from-area-without-type game area-id tree-type) [])
                                   updated-tree-map)}
        updated-world (merge (:world game) {:areas [updated-area]})]
    (merge game {:world updated-world})))
