## Kotlin DSL for your automatic tests

This is an example project for [Building a Kotlin DSL for your automatic tests](https://zone84.tech/programming/building-a-kotlin-dsl-for-your-automatic-tests/)
article published at [zone84.tech](https://zone84.tech). You can run it and fully explore the presented solution.

The project shows a Kotlin DSL for setting up the test context in the 'given' section. The DSL hides the implementation
details and focuses on the business scenario. The example domain is a simple automation system that monitors various sensors
(temperature, humidity, etc.) and triggers actions to the connected devices, such as fans or heaters. Certain aspects
are intentionally simplified in order to keep the code in reasonable size.

Also, the example shows one place, where (contrary to the recommendation in the article) we used domain entities
in the DSL directly. Can you spot the consequences for unit and integration tests?

Author: Tomasz Jędrzejewski

License: MIT License
