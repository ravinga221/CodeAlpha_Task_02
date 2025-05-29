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
}