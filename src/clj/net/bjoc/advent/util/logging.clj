(ns net.bjoc.advent.util.logging)

(def ^:private global-level (atom :info))

(defn set-level! [level]
  (reset! global-level level))

(defn get-level [] @global-level)

(def ^:private severity
  {:trace 0
   :debug 1
   :info 2
   :warn 3
   :error 4
   :fatal 5})

(defn- log [level & args]
  (when (<= (severity @global-level) (severity level))
    (apply println args)))

(def trace (partial log :trace))
(def debug (partial log :debug))
(def info (partial log :info))
(def warn (partial log :warn))
(def error (partial log :error))
(def fatal (partial log :fatal))
