(ns zork-fortress.cmds.look
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]
            [clojure.string :as str]))

(t/ann look-cmd-help String)
(def look-cmd-help
  "Use look to look around.")

(t/ann get-tree-info [t2/Game -> String])
(defn get-tree-info
  "Return a string with tree info for the area."
  [game]
  (let [tree-counts (g/get-current-area-tree-counts game)]
    (str/join "\n" (t/for [[type count] :- '[String t/Int] tree-counts]
                     :- String
                     (str "You see " count " " type 
                          (if (= 1 count) " tree." " trees."))))))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  (let [area (g/get-current-area game)]
    (str (:name area) " [" (:type area) "]\n\n"
         (get-tree-info game))))

