-keep class com.acszo.redomi.model.Providers { *; }
-keep class com.acszo.redomi.model.Link { *; }
-keep class com.acszo.redomi.model.SongInfo { *; }
-keep class com.acszo.redomi.model.Release { *; }
-keep class com.acszo.redomi.model.Asset { *; }

-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation