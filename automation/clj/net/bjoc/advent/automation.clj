(ns net.bjoc.advent.automation
  (:require [clojure.java.io :as io]))

(def impl-template
"(ns net.bjoc.advent.year-%s.day-%s
  (:use [net.bjoc.advent.util.misc :only [zip]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))
  
;;;

;;;

(advent/defrunner 1 identity \"\")
(advent/defrunner 2 identity \"\")")

(def test-template
"(ns net.bjoc.advent.year-%s.day-%s-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-%s.day-%s]))")

(def impl-filename-stem "src/clj/net/bjoc/advent/")
(def test-filename-stem "test/clj/net/bjoc/advent/")
(def data-filename-stem "data/")

(defn create-starters
  "Entry point to Advent of Code runner."
  [{:keys [year day] :as opts}]
  (when-not (and year day)
    (throw (ex-info "Year and day not specified."
                    {:command-line-options opts})))
  (let [impl-filename (str impl-filename-stem (format "year_%s/day_%s.clj" year day))
        test-filename (str test-filename-stem (format "year_%s/day_%s_test.clj" year day))
        data-filename (str data-filename-stem (format "year_%s/day_%s.input" year day))]
    (doseq [filename [impl-filename test-filename data-filename]]
      (when (.exists (io/file filename))
        (throw (ex-info "Stopping! File already exists!"
                        {:filename filename}))))
    (spit impl-filename (format impl-template year day))
    (spit test-filename (format test-template year day year day))
    (spit data-filename "")))
