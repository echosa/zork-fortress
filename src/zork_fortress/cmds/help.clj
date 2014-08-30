(ns zork-fortress.cmds.help
  (:require [clojure.core.typed :as t]
            [clojure.walk :as w])
  (:import [clojure.lang Namespace]))

(t/ann default-help String)
(def default-help 
  "Which command you like help on? Try \"help <command>\" or \"help commands\" to see a list of commands.")

(t/ann invalid-command-msg String)
(def invalid-command-msg 
  "No help for that topic.")

(t/ann available-commands-list [-> String])
(defn available-commands-list
  "Returns a list of command for which help is available."
  []
  (let [ns-prefix "zork-fortress.cmds."
        cmd-ns (sort
                (comparator (t/fn [ns1 :- Namespace ns2 :- Namespace] :- t/Num (compare (str ns1) (str ns2))))
                (filter (t/fn [ns :- Namespace] :- t/Bool
                          (and (= -1 (.indexOf (str ns) "-test")) (not= -1 (.indexOf (str ns) ns-prefix)))) (all-ns)))]
    (str (w/walk (fn [ns] (str (subs (str ns) (count ns-prefix)) "\n"))
                 (fn [s] {:pre [((t/pred (t/U nil (t/Coll t/Any))) s)]} (apply str s))
                 cmd-ns))))

(t/ann help-cmd [& :optional {:args (t/Vec String)} -> String])
(defn help-cmd
  "Shows helpful information to the user."
  [& {:keys [args]}]
  (if (= (first args) "commands")
    (available-commands-list)
    (if (= (first args) "foobar")
      invalid-command-msg
      default-help)))
