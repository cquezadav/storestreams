package storestreams.producer

object Constants {

  val DeviceType = Seq("tablet", "phone", "pc", "mac")
  val OperatingSystem = Seq("windows", "osx", "linux", "unix", "chrome")
  val MobileOperatingSystem = Seq("android", "osx_mobile", "windows_mobile", "linux_mobile")
  val VisitOrigin = Seq("search_engine_ad", "app_ad", "website_ad", "email_ad", "text_msg_ad", "alexa", "google_home")
  val Department = Seq((1, "TV & Video"), (5000, "Home Audio & Theater"), (10000, "Camera, Photo & Video"), (15000, "Cell Phones & Accessories"),
    (20000, "Headphones"), (25000, "Video Games"), (30000, "Bluetooth & Wireless Speakers"), (35000, "Car Electronics"), (40000, "Musical Instruments"),
    (45000, "Internet, TV and Phone Services"), (50000, "Wearable Technology"), (55000, "Electronics Showcase"), (60000, "Computers & Tablets"),
    (65000, "Monitors"), (70000, "Accessories"), (75000, "Networking"), (80000, "Drives & Storage"), (85000, "Computer Parts & Components"),
    (90000, "Software"), (95000, "Printers & Ink"), (100000, "Office & School Supplies"), (105000, "Trade In Your Electronics"))
  val Action = Seq("click_product", "wishlist", "save_for_later", "purchase", "add_to_cart")
  val PaymentType = Seq("credit card", "gift card", "paypal", "visa checkout")
  val ShipmentType = Seq("shipment", "pickup")
  val UserLocations = {
    for {
      line <- scala.io.Source.fromURL(getClass.getResource("/us_states.csv")).getLines().toVector
      values = line.split(",").map(_.trim)
    } yield values(2)
  }
}