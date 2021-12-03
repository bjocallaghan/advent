(ns net.bjoc.advent.util.binary)

(defn read-binary-string
  "Convert a string in binary format to the corresponding integer."
  [s]
  (when-not (re-find #"^[01]+$" s)
    (throw (NumberFormatException.
            (str "Cannot interpret as a binary string: " s))))
  (loop [n 0
         s s]
    (if (empty? s)
      n
      (recur (+ (* 2 n) (case (first s) \1 1 \0 0))
             (rest s)))))
