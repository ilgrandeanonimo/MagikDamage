# MagikDamage
**MagikDamage** is a health variation indicator plugin for [PaperMC](https://papermc.io/), designed to display sleek, 
customizable indicators handled entirely via packets.

![MagikDamage Banner](assets/title.png)

## ✨ Features

* **Packet-based & Asynchronous:** Indicators are sent directly to clients using packets. This ensures **zero lag** on the main server thread and no "real" entities cluttering your world.
* **Advanced Filtering System:** Complete control over when and how indicators appear. You can filter by:
    * **Event Type:** Damage dealt or health regained (healing).
    * **Entity Type:** Specific mobs or players (e.g., different visuals for Zombies vs. Bosses).
    * **Cause:** Differentiate visuals based on the source (e.g., Projectiles, Fall Damage, Magic, etc.).
* **Fully Customizable Displays:** Leveraging *Text Display* entities, you can fine-tune colors (ARGB), shadows, transparency, and text scales to match your server's aesthetic.
* **Localized Formatting:** Built-in support for multiple numeric formats (e.g., toggling between European and American decimal separators).

## 🛠️ Requirements & Compatibility

* **Version:** Should work on Paper 1.20.5 or higher (utilizes modern Display Entity APIs).
* **Java:** Requires **Java 21** or later.
> **Note:** It has been tested just on 1.21.4+ version and may not work on older version. If you encounter any problems, don't hesitate to open an issue on GitHub or ask for help on my Discord server.

## ⚙️ Configuration (`settings.yml`)

The plugin uses a flexible filtering logic. If no specific filter matches an event, it will automatically look for a fallback indicator.

```yaml
# MagikDamage by IlGrandeAnonimo

formats:
  american:
    format: '###.##' 
    decimalsSeparator: .
    positivesSign: true
    negativesSign: true
  european:
    format: '###.##'
    decimalsSeparator: ','
    positivesSign: true
    negativesSign: true

indicators:
  default_harm:
    format: european
    filters:
      - minecraft:event=damage # Options: 'damage' or 'regain'
    display:
      background: '#00000000' # ARGB Color
      foreground: '#ffffffff'
      shadow: true
      seeThrough: true
      animate: true
```
> **TIP**
> If the filters field is empty (`{}`), the indicator acts as a fallback. If multiple indicators have no filters, the last one defined in the config will be prioritized.

### Filter
Filters are made out of 2 parts: the identifier (a namespaced key, for example `minecraft:event`), and the value 
(only some filters requires it). There are three built-in filters:
* `minecraft:event` Require an argument. The argument must only be `damage` or `regain`
* `minecraft:cause` Require an argument. The argument must be a `EntityDamageEvent.DamageCause` or `EntityRegainHealthEvent.RegainReason`
* `minecraft:entity` Require an argument. The argument must be an `EntityType`

#### API
You can write your own filters. Just add the JitPack repository:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
then, add to your project's dependencies:
```groovy
dependencies {
    compileOnly 'com.github.ilgrandeanonimo:MagikDamage:1.0.0-beta2'
}
```

then you can create your filter by implementing the `Filter` interface:
```java
// This is the implementation of the built-in `minecraft:event` filter.
public class EventFilter implements Filter {
    @Override
    public String name() {
        return "event";
    }

    @Override
    public boolean filter(String parameters, @Nullable EntityEvent event) {
        if(Objects.equals(parameters, "regain")) {
            return event instanceof EntityRegainHealthEvent;
        } else if (parameters.equalsIgnoreCase("damage")) {
            return event instanceof EntityDamageEvent;
        }
        return false;
    }
}
```
