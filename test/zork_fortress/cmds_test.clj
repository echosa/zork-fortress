(ns zork-fortress.cmds-test
  (:require [clojure.test :refer :all]
            [zork-fortress.cmds :refer :all]))

(deftest test-running-command-without-previous-turn
  (testing "Look command should return message."
    (is (= {:player {:name "Player"}
            :world {:areas [{:name "First Area" :type "Plains"}]}
            :last-turn {:command 'look
                        :response "You see nothing."}
            :turn-history []}
           (run-cmd {:player {:name "Player"}
                     :world {:areas [{:name "First Area" :type "Plains"}]}
                     :turn-history []}
                    'look)))))

(deftest test-running-command-with-previous-turn
  (testing "Look command should return message."
    (is (= {:player {:name "Player"}
            :world {:areas [{:name "First Area" :type "Plains"}]}
            :last-turn {:command 'look
                        :response "You see nothing."}
            :turn-history [{:command 'prev
                            :response "Previous command."}]}
           (run-cmd {:player {:name "Player"}
                     :last-turn {:command 'prev
                                 :response "Previous command."}
                     :world {:areas [{:name "First Area" :type "Plains"}]}
                     :turn-history []}
                    'look)))))

(deftest test-running-invalid-command
  (testing "Invalid command should respond as such."
    (is (= {:player {:name "Player"}
            :world {:areas [{:name "First Area" :type "Plains"}]}
            :last-turn {:command 'foobar
                        :response "Invalid command."
                        :invalid true}
            :turn-history []}
           (run-cmd {:player {:name "Player"}
                     :world {:areas [{:name "First Area" :type "Plains"}]}
                     :turn-history []}
                    'foobar)))))

(deftest test-valid-commands-should-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (is (= {:player {:name "Player"}
            :world {:areas [{:name "First Area" :type "Plains"}]}
            :last-turn {:command 'look
                        :response "You see nothing."}
            :turn-history [{:command 'first
                            :response "First message."}
                           {:command 'previous
                            :response "Previous message."}]}
           (run-cmd {:player {:name "Player"}
                     :world {:areas [{:name "First Area" :type "Plains"}]}
                     :last-turn {:command 'previous
                                 :response "Previous message."}
                     :turn-history [{:command 'first
                                     :response "First message."}]}
                    'look)))))

(deftest test-invalid-commands-should-not-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (is (= {:player {:name "Player"}
            :world {:areas [{:name "First Area" :type "Plains"}]}
            :last-turn {:command 'look
                        :response "You see nothing."}
            :turn-history [{:command 'first
                            :response "First message."}]}
           (run-cmd {:player {:name "Player"}
                     :world {:areas [{:name "First Area" :type "Plains"}]}
                     :last-turn {:command 'foobar
                                 :response "Invalid command."
                                 :invalid true}
                     :turn-history [{:command 'first
                                     :response "First message."}]}
                    'look)))))

