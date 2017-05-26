(ns dmm.examples.oct-19-2016-experiment
  (:require [dmm.core :as dc
             :refer [v-accum v-identity
                     down-movement up-movement
                     rec-map-sum]]))


; trying to build a very small network to reproduce the essence of
; Project Fluid Aug 27, 2016 experiment

(def init-matrix
  {v-accum {:self {:accum {v-accum {:self {:single 1}}}}}})

(def update-1-matrix-hook
  {v-identity {:update-1 {:single {v-identity {:update-1 {:single 1}}}}}})

(def update-2-matrix-hook
  {v-identity {:update-2 {:single {v-identity {:update-2 {:single 1}}}}}})

(def update-3-matrix-hook
  {v-identity {:update-3 {:single {v-identity {:update-3 {:single 1}}}}}})

(def start-update-of-network-matrix
  {v-accum {:self {:delta {v-identity {:update-1 {:single 1}}}}}})

(def start-matrix
  (rec-map-sum
   init-matrix update-1-matrix-hook update-2-matrix-hook
   update-3-matrix-hook start-update-of-network-matrix))


(def update-1-matrix
  (rec-map-sum {v-accum {:self {:delta {v-identity {:update-1 {:single -1}}}}}}
               {v-accum {:self {:delta {v-identity {:update-2 {:single 1}}}}}}))

(def update-2-matrix
  (rec-map-sum {v-accum {:self {:delta {v-identity {:update-2 {:single -1}}}}}}
               {v-accum {:self {:delta {v-identity {:update-3 {:single 1}}}}}}))

(def update-3-matrix
  (rec-map-sum {v-accum {:self {:delta {v-identity {:update-3 {:single -1}}}}}}
               {v-accum {:self {:delta {v-identity {:update-1 {:single 1}}}}}}))


; (def init-output {v-accum {:self {:single init-matrix}}})

(def init-output
  (rec-map-sum {v-accum {:self {:single start-matrix}}}
               {v-identity {:update-1 {:single update-1-matrix}}}
               {v-identity {:update-2 {:single update-2-matrix}}}
               {v-identity {:update-3 {:single update-3-matrix}}}))

(defn extract-matrix [current-output]
  (((current-output v-accum) :self) :single))

(defn extract-delta [current-output]
  ((((extract-matrix current-output) v-accum) :self) :delta))


;;; recording the experiment here on Oct 20 after the switch from
;;; accum to (var accum), and from identity to (var identity)
;;;
;;; also the difference here is that we are using iter-apply-fns to
;;; run network steps and that we actually re-run the network from the
;;; start each time (just because it's less typing and because we can)

(comment
  (->> (dc/iter-apply-fns init-output down-movement up-movement)
       rest (dc/map-every-other extract-delta)
       (take 20)
       (filter v-identity)
       clojure.pprint/pprint)
  user>
  ({#'clojure.core/identity {:update-2 {:single 1}}}
   {#'clojure.core/identity {:update-3 {:single 1}}}
   {#'clojure.core/identity {:update-1 {:single 1}}}
   {#'clojure.core/identity {:update-2 {:single 1}}}
   {#'clojure.core/identity {:update-3 {:single 1}}}
   {#'clojure.core/identity {:update-1 {:single 1}}}
   {#'clojure.core/identity {:update-2 {:single 1}}}
   {#'clojure.core/identity {:update-3 {:single 1}}}
   {#'clojure.core/identity {:update-1 {:single 1}}}
   {#'clojure.core/identity {:update-2 {:single 1}}}))




; user=> (extract-delta init-output)
; {#'clojure.core/identity {:update-1 {:single 1}}}
; user=> (extract-delta (first (drop 2 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-2 {:single 1}}}
; user=> (extract-delta (first (drop 4 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-3 {:single 1}}}
; user=> (extract-delta (first (drop 6 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-1 {:single 1}}}
; user=> (extract-delta (first (drop 8 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-2 {:single 1}}}
; user=> (extract-delta (first (drop 10 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-3 {:single 1}}}
; user=> (extract-delta (first (drop 12 (iter-apply-fns init-output down-movement up-movement))))
; {#'clojure.core/identity {:update-1 {:single 1}}}
