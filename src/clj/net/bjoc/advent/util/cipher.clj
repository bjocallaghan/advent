(ns net.bjoc.advent.util.cipher
  (:require [clojure.string :as str]))

(def lowercase "abcdefghijklmnopqrstuvwxyz")

(defn rot-n-fn
  [n
   & {:keys [charset]
      :or {charset lowercase}
      :as options}]
  (let [size (count charset)
        member? (set charset)
        cutdown #(mod % size)]
    (fn [c]
      ;; avoid infinite loop
      (when-not (member? c)
        (throw (ex-info "Input to be rotated not found in charset."
                        {:input c
                         :charset charset})))
      (->> (cycle charset)
           (drop-while #(not= % c))
           (drop (cutdown n))
           first))))

(defn rot-n
  "This is a generalized solution--not optimized for speed. If speed becomes an
  issue, consider using a more primative approach."
  [n c
   & {:keys [charset]
      :or {charset lowercase}
      :as options}]
  ((rot-n-fn n options) c))
