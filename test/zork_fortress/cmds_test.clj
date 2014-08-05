(ns zork-fortress.cmds-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-commands-should-set-last-turn
  (testing "Look command should return message."
    (let [game (h/get-test-game)]
      (is (= nil (:last-turn game))
          (= {:command {:trigger 'look} :response "You see nothing."}
             (:last-turn (run-cmd game {:trigger 'look})))))))

(deftest test-commands-should-set-history
  (testing "Look command should return message."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'prev}
                                            :response "Previous command."})]
      (is (= [] (:turn-history game))
          (= [{:command {:trigger 'prev} :response "Previous command."}]
             (:turn-history (run-cmd game {:trigger 'look})))))))

(deftest test-invalid-commands-are-marked-as-such
  (testing "Invalid command should respond as such."
    (let [game (h/get-test-game)]
      (is (= {:command {:trigger 'foobar}
              :response "Invalid command."
              :invalid true}
              (:last-turn (run-cmd game {:trigger 'foobar})))))))
