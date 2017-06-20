(ns zork-fortress.cmds.look
  (:require [zork-fortress.game :as g]
            [clojure.string :as str]))

(def look-cmd-help
  "Use look to look around.")

(defn get-tree-info
  "Return a string with tree info for the area."
  [game]
  (let [tree-counts (g/get-current-area-tree-counts game)]
    (str/join "\n" (for [[type count] tree-counts]
                     (when (not= 0 count)
                       (str "You see " count " " type
                            (if (= 1 count) " tree." " trees.")))))))

(defn look-cmd
  "The look command."
  [game]
  (let [area (g/get-current-area game)]
    (str/trim
     (str (:name area) " [" (:type area) "]\n\n"
          (get-tree-info game)))))

