(ns zork-fortress.cmds.chop-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.cmds.chop :as chop])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-chopping-with-no-trees-should-display-an-error-msg
  (testing "Trying to chop trees where there are none should show an error message."
    (let [game (h/get-test-game :no-trees true)]
      (is (= chop/no-trees-msg
           (:response (:last-turn (run-cmd game {:trigger 'chop}))))))))
