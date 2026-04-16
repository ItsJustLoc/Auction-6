# Auction6 — UI/UX Redesign Plan for Claude Code
> Paste this entire file into your Claude Code session and say: "Follow this plan to redesign the UI"

---

## 1. Design System: Retro-Modern Marketplace

### Visual Identity
- **Style**: Retro-Futurism meets Flat Design — clean card layouts with bold typography, subtle vintage warmth, fast and snappy feel
- **Mood**: eBay-era familiarity + modern speed. Think bold type, structured cards, warm neutrals
- **NO**: gradients, glassmorphism, neon, excessive rounded corners everywhere, generic blue SaaS

### Color Palette (Jetpack Compose Material 3 tokens)
```kotlin
// ui/theme/Color.kt
val RetroOrange     = Color(0xFFE8622A)   // Primary CTA — bid button, Save Listing
val RetroCream      = Color(0xFFF5F0E8)   // Background primary (warm off-white)
val RetroCard       = Color(0xFFEDEAE2)   // Card surface
val RetroInk        = Color(0xFF1A1612)   // Primary text
val RetroMuted      = Color(0xFF6B6560)   // Secondary text (category, subtitles)
val RetroGreen      = Color(0xFF2D7A4F)   // Payment AUTHORIZED, Delivered status
val RetroAmber      = Color(0xFFB87333)   // Auction timer, bid price
val RetroRed        = Color(0xFFC0392B)   // Returned status, errors
val RetroBorder     = Color(0xFFD4CFC6)   // Card borders, dividers
val RetroBlue       = Color(0xFF2C5F8A)   // Links, "via Buy Now", navigation buttons
val SurfaceDark     = Color(0xFF1C1915)   // Dark mode background
val CardDark        = Color(0xFF262320)   // Dark mode card
```

### Typography (Jetpack Compose)
```kotlin
// ui/theme/Type.kt
// Use system font with intentional weight variation — feels fast, not decorative
val AppTypography = Typography(
    displayLarge  = TextStyle(fontWeight = FontWeight.Black,  fontSize = 28.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold,  fontSize = 20.sp),
    titleMedium   = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyMedium    = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelSmall    = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 0.8.sp, color = RetroMuted)
)
```

### Shape Tokens
```kotlin
// Cards: 10.dp corner radius — feels structured, not bubbly
// Buttons: Full pill (50.dp) for primary, 10.dp for secondary
// Category chips: 20.dp (pill)
// Input fields: 8.dp radius, NO full pill — looks like a form, not a toy
```

---

## 2. Screen-by-Screen Instructions

### Screen 1: Login / Main Screen
**Current problems**: Empty space top half, weak visual hierarchy, Login button too subtle vs Create Account

**Fix**:
```
- Add app name "AUCTION6" at top center — bold, large, RetroInk, slight letter-spacing
- Add tagline: "Buy & sell car parts fast" in RetroMuted below
- Move form to vertical center of screen (currently bottom-heavy)
- Email field: RetroCard background, RetroBorder stroke 1dp, 8dp radius, RetroInk text
- Password field: same style
- LOGIN button: full-width, RetroOrange fill, white bold text, 10dp radius
- CREATE ACCOUNT: full-width outline button, RetroBlue border + text, transparent fill
- Add 8dp vertical gap between the two buttons
```

### Screen 2: Marketplace (Main Listing Screen)
**Current problems**: Category chips wrap randomly, listing cards all look the same, "Your listing" badge is hard to see

**Fix — Category Chips (IMPORTANT)**:
```kotlin
// Use FlowRow from accompanist or Compose foundation
// Each chip = 1 item, chips wrap naturally 1-by-1 onto new lines
// Do NOT use a single horizontal scroll — all categories must be visible

@Composable
fun CategoryChipGroup(categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            FilterChip(
                selected = cat == selected,
                onClick = { onSelect(cat) },
                label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RetroInk,
                    selectedLabelColor = RetroCream,
                    containerColor = RetroCard,
                    labelColor = RetroMuted
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = cat == selected,
                    borderColor = RetroBorder,
                    selectedBorderColor = RetroInk
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
```

**Fix — Listing Cards**:
```kotlin
@Composable
fun ListingCard(listing: Listing, isYours: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = RetroCard),
        border = BorderStroke(1.dp, RetroBorder)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Left: photo thumbnail (80x80dp, 8dp radius)
            // OR placeholder box with category icon color if no photo
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(listing.title, style = titleMedium, color = RetroInk, modifier = Modifier.weight(1f))
                    if (isYours) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = RetroAmber.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, RetroAmber)
                        ) {
                            Text("YOUR LISTING", 
                                style = labelSmall.copy(color = RetroAmber, fontSize = 9.sp, letterSpacing = 1.sp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
                
                Text(listing.category, style = labelSmall, color = RetroBlue)
                
                Spacer(Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$${listing.startingPrice}", 
                        style = headlineMedium.copy(color = RetroAmber, fontWeight = FontWeight.Bold))
                    Text("  starting bid", style = bodyMedium.copy(color = RetroMuted))
                }
                
                // Show buy now price if present
                listing.buyNowPrice?.let {
                    Text("Buy Now: $$it", style = labelSmall.copy(color = RetroGreen))
                }
                
                // Countdown timer if auction active
                Text("⏱ ${listing.timeRemaining}", style = labelSmall.copy(color = RetroMuted))
            }
        }
    }
}
```

**Fix — Top Bar**:
```kotlin
// Current: "Log out" left, "+ List Item" right — awkward and functional-ugly
// New: 
// - App title "AUCTION6" centered, bold
// - Hamburger/avatar icon right for account menu (log out moves here)
// - FAB (Floating Action Button) bottom right: "+" in RetroOrange — standard Android pattern
// - Purchases / Sales as TabRow under the title, not orphan buttons
```

### Screen 3: Create Listing
**Current problems**: Fields are plain, category chips horizontally overflow, photo area empty

**Fix**:
```kotlin
// All text fields: same styled component
@Composable
fun AuctionTextField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RetroOrange,
            unfocusedBorderColor = RetroBorder,
            focusedLabelColor = RetroOrange,
            unfocusedContainerColor = RetroCard,
            focusedContainerColor = RetroCard
        )
    )
}

// Category section: same FlowRow chip pattern as marketplace screen
// Photo section: 
//   - Show selected image as 160x160dp rounded preview
//   - OR dashed border box with camera icon + "Add Photo" text if empty
//   - Tap to change photo

// Buttons:
//   - "Save Listing": RetroOrange, full-width, pill shape
//   - "Cancel": RetroMuted text only (TextButton), no border — less visual weight
```

### Screen 4: My Purchases
**Current problems**: Empty state is just text, no visual

**Fix**:
```kotlin
// Empty state component (reuse across Purchases and Sales)
@Composable
fun EmptyState(icon: ImageVector, title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, 
           modifier = Modifier.fillMaxSize().padding(40.dp),
           verticalArrangement = Arrangement.Center) {
        Icon(icon, contentDescription = null, 
             modifier = Modifier.size(64.dp), tint = RetroBorder)
        Spacer(Modifier.height(16.dp))
        Text(title, style = titleMedium, color = RetroInk)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = bodyMedium, color = RetroMuted, textAlign = TextAlign.Center)
    }
}

// Usage: EmptyState(Icons.Outlined.ShoppingBag, "No purchases yet", "Win an auction or use Buy Now to see your items here")
```

### Screen 5: My Sales
**Current problems**: Status colors correct (green = good) but "via Buy Now" looks like a link, not a tag

**Fix**:
```kotlin
@Composable
fun SaleCard(sale: Sale) {
    Card(...) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(sale.title, style = titleMedium, color = RetroInk)
            Text("Sold for: $${sale.price}", style = bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            
            Spacer(Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusTag(label = "Payment: ${sale.paymentStatus}", 
                          color = if (sale.paymentStatus == "AUTHORIZED") RetroGreen else RetroRed)
                StatusTag(label = "Shipping: ${sale.shippingStatus}",
                          color = when(sale.shippingStatus) {
                              "Delivered" -> RetroGreen
                              "Returned"  -> RetroRed
                              else        -> RetroAmber
                          })
                if (sale.viaBuyNow) {
                    StatusTag(label = "Buy Now", color = RetroBlue)
                }
            }
        }
    }
}

@Composable
fun StatusTag(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(label,
            style = labelSmall.copy(color = color, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}
```

---

## 3. Global Component Rules

### Navigation
- Move "Log out" into a profile/settings screen — never expose it on the main screen
- Use `TabRow` for Purchases / Sales navigation inside the Marketplace screen
- Use a bottom `FloatingActionButton` (RetroOrange) for "+ List Item"

### Spacing System (use consistently — no random padding)
```
4dp  = micro gap (between a label and its value)
8dp  = small gap (between chips, between icon and text)
12dp = field internal padding
16dp = screen horizontal padding (all screens)
24dp = section separator
40dp = empty state padding
```

### Loading / States
- Use `CircularProgressIndicator(color = RetroOrange)` for all loading states
- Skeleton loaders for cards: gray animated shimmer boxes at card height while fetching
- Pull-to-refresh: standard Compose pull-to-refresh with `RetroOrange` indicator

### Error handling
- Snackbar at bottom: RetroInk background, white text, RetroOrange action button
- Not a dialog — dialogs are for destructive confirmations only

---

## 4. Install ui-ux-pro-max Skill in Your Project

Run these two commands inside your project directory in Claude Code terminal:

```bash
npm install -g uipro-cli
uipro init --ai claude
```

Then when prompting Claude Code for any UI task, prefix with:
```
Using the ui-ux-pro-max skill with Jetpack Compose stack and the Auction6 retro-modern color system defined in AUCTION6_UI_PLAN.md, please...
```

---

## 5. Implementation Order (Suggested)

1. **Theme first** — Create `Color.kt`, `Type.kt`, `Theme.kt` with the tokens above. Apply `AuctionTheme` to `MainActivity`. Everything else follows from this.
2. **Shared components** — `AuctionTextField`, `StatusTag`, `EmptyState`, `ListingCard`, `CategoryChipGroup`. Build these as standalone composables in a `ui/components/` folder.
3. **Marketplace screen** — Replace current list with `ListingCard`, add `FlowRow` categories, move FAB in.
4. **Create Listing screen** — Swap fields for `AuctionTextField`, fix category chips, improve photo picker UX.
5. **Sales screen** — Apply `SaleCard` with `StatusTag` badges.
6. **Login screen** — Add app title, fix button hierarchy.
7. **Purchases screen** — Add `EmptyState` component.

---

## 6. What to Tell Claude Code Each Session

Start each Claude Code session with:

```
I'm working on Auction6, a car parts auction app in Kotlin/Jetpack Compose. 
The design system is in AUCTION6_UI_PLAN.md — follow it strictly for colors, 
spacing, and component patterns. The style is retro-modern marketplace: 
warm cream backgrounds, RetroOrange CTAs, structured cards with 1dp borders. 
No gradients, no glassmorphism, no generic Material You purple.
```

This ensures Claude Code never defaults to generic Material 3 teal/purple styling.
