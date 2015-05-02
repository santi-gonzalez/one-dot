# one-dot bugs

Simple game for Android, based on
[one-dot enemies](https://itunes.apple.com/en/app/one-dot-enemies/id306930506) for iOS. For the sake
of convenience, simplicity and compatibility this game is made only using basic classes from the
Android framework, such as Views, xml resources, Bitmaps, ...
In fact, you only need to add the `OneDotView` to any layout, perform calls in some specific
lifecycle callbacks, and register a listener to get notified of game events.

## COMPATIBILITY

The module is build targeting API level 21, and compatible from API level 14, but (despite it's not
yey tested) it should be compatible from at least API level 10.

## USAGE

The usage is pretty simple. You can always take a look at the sample included in this repository to
get an idea on the steps to be made.

* First you have to do is include the module in your project. Provided you already copied it (still
not published to a gradle repository) add this line to your build gradle:

        dependencies {
            compile project(':mod-one-dot')
        }

* You now have to include the `OneDotView` to a layout, like so:

        <cat.santi.mod.onedot.OneDotView
            android:id="@+id/od__main__one_dot_view"
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:odvDebug="true"
            app:odvThumbRadius="17"
            app:odvScore="0"
            app:odvSurface="#fff"/>

* Last, bind the view with the container lifecycle (either an `Activity` or a `Fragment`), like
this (example with a `Fragment`):

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflate...
            mOneDotView = (OneDotView) view.findViewById(R.id.oneDotView)
            mOneDotView.setCallbacks(this);
        }

        @Override
        public void onResume() {
            ...
            mOneDotView.onResume();
        }

        @Override
        public void onPause() {
            ...
            mOneDotView.onPause();
        }

        @Override
        public void onDestroyView() {
            ...
            mOneDotView.onDestroy();
        }

## SUPPORTED VIEW ATTRIBUTES

Currently supported view attributes (either by `xml` or programatically) are:

* `odvScore` (`+setScore(int):void` `+getScore():int`)
  Set the initial score.

* `odvThumbRadius` (`+setThumbRadius(float):void` `+getThumbRadius():float`)
  Set the thumb radius to smash dots, in dip.

* `odvSurface` (`+setSurface(int):void` `+getSurface():int`)
  Set the surface solor. (_same effect of calling `View#setBackground()`_)

* `odvDebug` (`+setDebug(boolean):void` `+isDebug():boolean`)
  Show debug graphics, toasts, and logs.

## YET TO IMPLEMENT

I've been working for just two days (not even full days) in this project, so there are some dynamics
still to be implemented. As of version 0.2, this is the list of features yet to implement:

* More in-game callbacks (dot smashed, game paused/resumed, ...)
* Automatic dot banish and regeneration.
* More statistics (like accuracy, banished dots, ...)
* Default views to show controls (start, pause, reset, ...) and statistics.
* _Publish the module to a gradle repository._

## KNOWN ISSUES

The way AI and game surface are implemented allows _dots_ to travel any direction as long, as the
destiny is inside the allowed borders. This means that the _dots_ tend to gather to the view
corners after some time. Might want to work more on it.

![Dots gathering on border issue](https://raw.github.com/santi-gonzalez/one-dot/master/images/Screenshot_2015-05-02-17-16-58.png)
> TODO: Add correct image link

## LICENSE

    Copyright 2015 Santiago Gonzalez Bertran

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
