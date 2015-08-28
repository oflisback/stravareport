# Strava report

This clojure script uses the Strava Java API https://github.com/danshannon/javastravav3api/ to fetch
activities for a given year and produce a google chart diagram showing distances per month and disciplines.

Since I have been doing running, cross-country skiing, roller skiing, bike riding and swimming, these are the reported disciplines. But it
should be easy to customize.

## Installation

Have a proper clojure environment and leinigen installed.

Also copy config.clj.template to config/config.clj and set your own values.

TODO: Improve.

## Usage

    $ java -jar stravaclj-0.1.0-standalone.jar <year> <output-file>

## Example

An example with my data can be seen here: http://www.bolland.nu/strava2015.html

## License

Copyright © 2015 Ola Flisbäck

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
