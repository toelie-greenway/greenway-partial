package greenway_myanmar.org;

import com.airbnb.epoxy.EpoxyDataBindingLayouts;
import com.airbnb.epoxy.EpoxyDataBindingPattern;

@EpoxyDataBindingLayouts({
        R.layout.epoxy_loading_state
})
@EpoxyDataBindingPattern(rClass = R.class, layoutPrefix = "epoxy_layout")
interface EpoxyDataBindingConfig {
}