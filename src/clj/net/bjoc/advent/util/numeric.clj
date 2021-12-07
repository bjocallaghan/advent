(ns net.bjoc.advent.util.numeric)

(defn int-str?
  "Return truthy if the input is a parsable integer string."
  [x]
  (and (string? x)
       (re-find #"^-?\d+$" x)))

(def parse-int "Convert a string to an integer." #(Integer/parseInt %))

(defn int-seq
  "Return an infinite sequence of integers."
  [starting-n]
  (lazy-seq (cons starting-n (int-seq (inc starting-n)))))

(defn digit-seq
  "Return a sequence of the single digits of a given number."
  [n]
  (loop [n n
         digits []]
    (if (zero? n)
      (reverse digits)
      (recur (quot n 10) (conj digits (mod n 10))))))

(defn exp
  "Raise a number to a power."
  [number exponent]
  (reduce * (repeat exponent number)))

(defn TRIAL_DIVISION*not-prime?
  "Return truthy for a non-prime number.

  The return value is generally (but not guaranteed) to be a divisor.

  Uses the trial division method."
  [n]
  (cond
    (= n 2) nil
    (= n 3) nil
    (< n 2) -1
    (even? n) 2
    :trial-division (let [limit (-> n Math/sqrt int inc)]
                      (loop [divisor 3]
                        (if (zero? (rem n divisor))
                          divisor
                          (when (< divisor limit)
                            (recur (+ divisor 2))))))))

(def TRIAL_DIVISION*prime? "Return truthy for a prime number."
  (complement TRIAL_DIVISION*not-prime?))

;;; defaults

(def prime? "Returns truthy for a prime number."
  TRIAL_DIVISION*prime?)

(def not-prime? "Return truthy for a non-prime number."
  TRIAL_DIVISION*not-prime?)
