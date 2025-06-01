import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class StockTradingPlatform 
{
	private Map<String, Stock> availableStocks;
    private Map<String, PortfolioItem> portfolio;
    private double cashBalance;
    private List<Transaction> transactionHistory;
    private LocalTime marketOpen;
    private LocalTime marketClose;
    private LocalDateTime currentTime;
	
	 public StockTradingPlatform() 
	{
        initializeStocks();
        this.portfolio = new HashMap<>();
        this.cashBalance = 10000.00;
        this.transactionHistory = new ArrayList<>();
        this.marketOpen = LocalTime.of(9, 30);
        this.marketClose = LocalTime.of(16, 0);
        this.currentTime = LocalDateTime.now();
    }
	
	private void initializeStocks() 
	{
        availableStocks = new HashMap<>();
        availableStocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 185.00, 0.02));
        availableStocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 145.50, 0.018));
        availableStocks.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 420.00, 0.015));
        availableStocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 180.00, 0.022));
        availableStocks.put("TSLA", new Stock("TSLA", "Tesla Inc.", 175.00, 0.025));
        availableStocks.put("NFLX", new Stock("NFLX", "Netflix Inc.", 620.00, 0.02));
        availableStocks.put("META", new Stock("META", "Meta Platforms", 485.00, 0.019));
        availableStocks.put("NVDA", new Stock("NVDA", "NVIDIA Corp.", 950.00, 0.023));
    }
	
	public void updateMarketData() 
	{
        Random random = new Random();
        for (Stock stock : availableStocks.values())
		{
            double changePercent = -stock.getVolatility() + 
                                 random.nextDouble() * 2 * stock.getVolatility();
            double newPrice = stock.getPrice() * (1 + changePercent);
            newPrice = Math.max(0.01, newPrice);
            stock.setPrice(Math.round(newPrice * 100.0) / 100.0);
        }
    }
	
	public boolean isMarketOpen() 
	{
        LocalTime now = currentTime.toLocalTime();
        DayOfWeek day = currentTime.getDayOfWeek();
        return !now.isBefore(marketOpen) && 
               !now.isAfter(marketClose) && 
               day != DayOfWeek.SATURDAY && 
               day != DayOfWeek.SUNDAY;
    }
	
	public String buyStock(String symbol, int quantity) 
	{
        if (!isMarketOpen()) 
		{
            return "Market is closed. Trading available Mon-Fri 9:30AM-4:00PM.";
        }

        if (!availableStocks.containsKey(symbol)) 
		{
            return "Stock " + symbol + " not found.";
        }

        Stock stock = availableStocks.get(symbol);
        double totalCost = stock.getPrice() * quantity;

        if (totalCost > cashBalance) 
		{
            return "Insufficient funds. Needed: $" + totalCost + ", Available: $" + cashBalance;
        }

        cashBalance -= totalCost;
        
        if (portfolio.containsKey(symbol)) 
		{
            PortfolioItem item = portfolio.get(symbol);
            item.setQuantity(item.getQuantity() + quantity);
            item.setAveragePrice(
                (item.getAveragePrice() * item.getQuantity() + totalCost) / 
                (item.getQuantity() + quantity)
            );
        } 
		else 
		{
            portfolio.put(symbol, new PortfolioItem(stock, quantity, stock.getPrice()));
        }

        transactionHistory.add(new Transaction(
            "BUY", symbol, quantity, stock.getPrice(), LocalDateTime.now()
        ));

        return "Successfully bought " + quantity + " shares of " + symbol + " at $" + stock.getPrice() + " per share.";
    }
	
	public String sellStock(String symbol, int quantity) 
	{
        if (!isMarketOpen()) 
		{
            return "Market is closed. Trading available Mon-Fri 9:30AM-4:00PM.";
        }

        if (!portfolio.containsKey(symbol))
		{
            return "You don't own any shares of " + symbol;
        }

        PortfolioItem item = portfolio.get(symbol);
        if (item.getQuantity() < quantity) 
		{
            return "Insufficient shares. You only own " + item.getQuantity() + " shares of " + symbol;
        }

        Stock stock = availableStocks.get(symbol);
        double totalValue = stock.getPrice() * quantity;

        cashBalance += totalValue;
        
        if (item.getQuantity() == quantity) 
		{
            portfolio.remove(symbol);
        } 
		else 
		{
            item.setQuantity(item.getQuantity() - quantity);
        }

        transactionHistory.add(new Transaction(
            "SELL", symbol, quantity, stock.getPrice(), LocalDateTime.now()
        ));

        return "Successfully sold " + quantity + " shares of " + symbol + " at $" + stock.getPrice() + " per share.";
    }
	
	public void displayPortfolio()
	{
        System.out.println("\n=== PORTFOLIO ===");
        System.out.printf("Cash Balance: $%.2f\n", cashBalance);
        
        if (portfolio.isEmpty()) 
		{
            System.out.println("No stocks in portfolio.");
            return;
        }

        System.out.println("\nStock Holdings:");
        System.out.printf("%-6s %-20s %-10s %-12s %-12s %-12s\n", 
                         "Symbol", "Name", "Quantity", "Avg Price", "Curr Price", "P/L");
        
        double totalValue = 0;
        double totalCost = 0;
        
        for (PortfolioItem item : portfolio.values())
		{
            Stock stock = item.getStock();
            double currentValue = stock.getPrice() * item.getQuantity();
            double costBasis = item.getAveragePrice() * item.getQuantity();
            double profitLoss = currentValue - costBasis;
            
            System.out.printf("%-6s %-20s %-10d $%-11.2f $%-11.2f $%-11.2f\n",
                            stock.getSymbol(),
                            stock.getName(),
                            item.getQuantity(),
                            item.getAveragePrice(),
                            stock.getPrice(),
                            profitLoss);
            
            totalValue += currentValue;
            totalCost += costBasis;
        }
        
        System.out.println("\nPortfolio Summary:");
        System.out.printf("Total Invested: $%.2f\n", totalCost);
        System.out.printf("Current Value:  $%.2f\n", totalValue + cashBalance);
        System.out.printf("Total P/L:      $%.2f\n", (totalValue + cashBalance) - 10000.00);
    }
	
	public void displayMarketData() 
	{
        System.out.println("\n=== MARKET DATA ===");
        System.out.printf("%-6s %-20s %-10s %-12s\n", "Symbol", "Name", "Price", "Change");
        
        for (Stock stock : availableStocks.values()) 
		{
            System.out.printf("%-6s %-20s $%-10.2f %+.2f%%\n",
                            stock.getSymbol(),
                            stock.getName(),
                            stock.getPrice(),
                            stock.getPriceChange() * 100);
        }
    }
	
	public void displayTransactionHistory() 
	{
        System.out.println("\n=== TRANSACTION HISTORY ===");
        System.out.printf("%-20s %-6s %-8s %-10s %-12s %-12s\n",
                         "Date/Time", "Action", "Symbol", "Quantity", "Price", "Amount");
        
        for (Transaction t : transactionHistory) 
		{
            System.out.printf("%-20s %-6s %-8s %-10d $%-11.2f $%.2f\n",
                            t.getDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")),
                            t.getAction(),
                            t.getSymbol(),
                            t.getQuantity(),
                            t.getPrice(),
                            t.getPrice() * t.getQuantity());
        }
    }
	
	class Stock 
	{
        private String symbol;
        private String name;
        private double price;
        private double volatility;
        private double previousPrice;

        public Stock(String symbol, String name, double price, double volatility) 
		{
            this.symbol = symbol;
            this.name = name;
            this.price = price;
            this.volatility = volatility;
            this.previousPrice = price;
        }

        public String getSymbol() 
		{ return symbol; }
        public String getName() 
		{ return name; }
        public double getPrice() 
		{ return price; }
        public double getVolatility() 
		{ return volatility; }
        public double getPriceChange() 
		{ return (price - previousPrice) / previousPrice; }

        public void setPrice(double price) 
		{
            this.previousPrice = this.price;
            this.price = price;
        }
    }
	
	class PortfolioItem 
	{
        private Stock stock;
        private int quantity;
        private double averagePrice;

        public PortfolioItem(Stock stock, int quantity, double averagePrice) 
		{
            this.stock = stock;
            this.quantity = quantity;
            this.averagePrice = averagePrice;
        }

        public Stock getStock() 
		{ return stock; }
        public int getQuantity() 
		{ return quantity; }
        public double getAveragePrice() 
		{ return averagePrice; }

        public void setQuantity(int quantity) 
		{ this.quantity = quantity; }
        public void setAveragePrice(double averagePrice) 
		{ this.averagePrice = averagePrice; }
    }
	
	class Transaction 
	{
        private String action;
        private String symbol;
        private int quantity;
        private double price;
        private LocalDateTime dateTime;

        public Transaction(String action, String symbol, int quantity, double price, LocalDateTime dateTime)
		{
            this.action = action;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
            this.dateTime = dateTime;
        }

        public String getAction() 
		{ return action; }
        public String getSymbol()
		{ return symbol; }
        public int getQuantity() 
		{ return quantity; }
        public double getPrice() 
		{ return price; }
        public LocalDateTime getDateTime() 
		{ return dateTime; }
    }
	
	public static void main(String[] args)
	{
        StockTradingPlatform platform = new StockTradingPlatform();
        Scanner scanner = new Scanner(System.in);
        
        while (true)
		{
            System.out.println("\n=== STOCK TRADING PLATFORM ===");
            System.out.println("1. View Market Data");
            System.out.println("2. View Portfolio");
            System.out.println("3. Buy Stocks");
            System.out.println("4. Sell Stocks");
            System.out.println("5. View Transaction History");
            System.out.println("6. Simulate Market Update");
            System.out.println("7. Exit");
            System.out.print("Select option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) 
			{
                case 1:
                    platform.displayMarketData();
                    break;
                case 2:
                    platform.displayPortfolio();
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int buyQty = scanner.nextInt();
                    System.out.println(platform.buyStock(buySymbol, buyQty));
                    break;
                case 4:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int sellQty = scanner.nextInt();
                    System.out.println(platform.sellStock(sellSymbol, sellQty));
                    break;
                case 5:
                    platform.displayTransactionHistory();
                    break;
                case 6:
                    platform.updateMarketData();
                    System.out.println("Market data updated.");
                    break;
                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}