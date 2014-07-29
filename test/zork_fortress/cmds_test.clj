(ns zork-fortress.cmds-test
  (:require [clojure.test :refer :all]
            [zork-fortress.cmds :refer :all]))

(deftest test-running-command-without-previous-turn
  (testing "Look command should return message."
    (is (= {:player {:name "Player"}
            :last-turn {:command 'look
                        :response "You see nothing."}}
           (run-cmd {:player {:name "Player"}} 'look)))))

(deftest test-running-command-with-previous-turn
  (testing "Look command should return message."
    (is (= {:player {:name "Player"}
            :last-turn {:command 'look
                        :response "You see nothing."}}
           (run-cmd {:player {:name "Player"}
                     :last-turn {:command 'prev
                                 :response "Previous command."}}
                    'look)))))