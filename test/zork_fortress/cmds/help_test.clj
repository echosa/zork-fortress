(ns zork-fortress.cmds.help-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.cmds.help :as help])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-default-help-should-prompt-for-command
  (testing "Typing 'help' without an argument should prompt for argument."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'help})))
                     help/default-help)))))

