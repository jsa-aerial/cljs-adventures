(defproject dmmproj "0.1.0-SNAPSHOT"
  :description "CLJS DMM with Quil..."
  :url "http://example.com/FIXME"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/clojurescript "1.9.521"
                  :exclusions [org.apache.ant/ant]]
                 [org.clojure/core.async "0.3.442"]
                 [quil "2.6.0"]
                 [re-com "2.0.0"]
                 [reagent "0.6.1"]
                 [cljsbuild "1.1.5"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.10"]]

  ;; This is required for lein-cljsbuild to hook into the default Leningen
  ;; tasks, e.g. the "lein compile" and "lein jar" tasks.
  :hooks [leiningen.cljsbuild]

  :figwheel
  {:css-dirs ["resources/public/stylesheets"]
   :server-port 3450}

  ;; All lein-cljsbuild-specific configuration is under the :cljsbuild
  ;; key.
  :cljsbuild
  {;; When using a ClojureScript REPL, this
   ;; option controls what port it listens on for
   ;; a browser to connect to.  Defaults to 9000.
   :repl-listen-port 9000

   :builds
   [{:id "dev"
     :source-paths ["src"]
     ;; If hooks are enabled, this flag determines whether
     ;; files from these :source-paths are added to the JAR
     ;; file created by "lein jar".
     :jar true
     :figwheel true
     :compiler
     {;;:main "my_sketch.core" ; works???? NO
      :optimizations :none ; :whitespace :simple :advanced
      :output-to "resources/public/js/main.js"
      ;; Sets the output directory for temporary files used during
      ;; compilation.  Must be unique among all :builds. Defaults
      ;; to "target/cljsbuild-compiler-X" (where X is a unique
      ;; integer): "target/my-compiler-output-"
      :output-dir "resources/public/js/cljs-dev"
      :pretty-print true
      ;; Determines whether comments will be output in the JavaScript
      ;; that can be used to determine the original source of the
      ;; compiled code. Defaults to false.
      :print-input-delimiter false
      ;; See
      ;; https://github.com/clojure/clojurescript/wiki/Source-maps
      :source-map true


      ;; New extern inference for :advanced opt.
      ;; writes externs file (see below) inferred_externs.js
      ;; Also need (set! *warn-on-infer* true) turns on inference warnings
      ;;:infer-externs true

      ;; Otherwise or additionally:
      ;; Configure externs files for external libraries.
      ;; Defaults to the empty vector [].

      ;; For this entry, and those below, you can find a very good
      ;; explanation at:
      ;; http://lukevanderhart.com/2011/09/30/using-javascript-and-clojurescript.html
      ;;:externs ["jquery-externs.js"]

      ;; Adds dependencies on external libraries.  Note that files in
      ;; these directories will be watched and a rebuild will occur if
      ;; they are modified. Defaults to the empty vector [].
      ;;:libs ["closure/library/third_party/closure"]

      ;; Adds dependencies on foreign libraries. Be sure that the url
      ;; returns a HTTP Code 200 Defaults to the empty vector [].
      ;;:foreign-libs [{:file "http://example.com/remote.js"
      ;;                :provides  ["my.example"]}]

      ;; New option to enable Google closure compiler to compile node
      ;; modules (like React)
      ;; Need to also
      ;; npm install --save-deps module-deps
      ;; yarn add --dev module-deps
      ;; :npm-deps {:react "15.4.2", :react-dom "15.4.2"}

      }}]})
