(ns zork-fortress.cmds.chop-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.cmds.chop :as chop])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-chopping-with-no-trees-should-display-an-error-msg
  (testing "Trying to chop trees where there are none should show an error message."
    (let [game (h/get-test-game :without-trees true)]
      (is (= chop/no-trees-msg
             (:response (:last-turn (run-cmd game {:trigger 'chop}))))))))

(deftest test-chopping-tree-should-display-log-count
  (testing "Chopping a tree should show the number of logs gotten from the tree."
    (let [game (h/get-test-game)]
      (is (= "Received 10 oak logs."
             (:response (:last-turn (run-cmd game {:trigger 'chop :args ["1"]}))))))))

;; TODO chopping tree with invalid ID
;; TODO chopping tree adds logs to inventory
;; TODO chopping tree removes tree from area
;; TODO single log should have singular message
