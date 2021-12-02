(ns net.bjoc.advent.core
  (:use [clojure.java.shell :only [sh]])
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre
             ;; :refer [log  trace  debug  info  warn  error  fatal  report
             ;;         logf tracef debugf infof warnf errorf fatalf reportf
             ;;         spy get-env]])
             :refer [trace debug info warn error fatal]]
            ))

(def this-ns-stem (->> *ns*
                       ns-name
                       name
                       (re-find #"(.*)\..*?$")
                       second))

(defn run
  "Entry point to Advent of Code runner."
  [{:keys [year day] :as opts}]
  (debug "Options:" opts)
  (let [ns-symbol (symbol (format "%s.year-%s.day-%s" this-ns-stem year day))]
    (require ns-symbol)
    (time
     (let [m (-> ns-symbol find-ns ns-map)]
       ((m 'part-1))
       ((m 'part-2))))))
