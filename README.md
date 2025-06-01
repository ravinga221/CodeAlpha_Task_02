# CodeAlpha_Task_02

# Stock Trading Platform - Java Application

## Overview
This is a Java-based stock trading simulation platform that allows users to:
- View real-time market data for popular stocks
- Buy and sell stocks with a virtual cash balance
- Track portfolio performance
- View transaction history
- Simulate market fluctuations

## Features
- Realistic stock market simulation with price volatility
- Portfolio tracking with profit/loss calculations
- Market hours enforcement (Mon-Fri 9:30AM-4:00PM)
- Transaction history logging
- Interactive console-based interface

## Getting Started

### Prerequisites
- Java JDK 8 or later
- Maven (for building)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/ravinga221/stock-trading-platform.git
   cd stock-trading-platform
   ```

2. Compile and run:
   ```bash
   javac StockTradingPlatform.java
   java StockTradingPlatform
   ```

### Usage
1. Run the application
2. Use the menu to navigate between options:
   - View Market Data: See current stock prices
   - View Portfolio: Check your holdings and performance
   - Buy Stocks: Purchase shares of available stocks
   - Sell Stocks: Sell shares you own
   - View Transaction History: See all past trades
   - Simulate Market Update: Refresh stock prices with random changes
   - Exit: Quit the application

## Technical Details
- Built with core Java (no external dependencies)
- Uses Java Collections Framework for data management
- Implements proper object-oriented design with:
  - Stock class for market data
  - PortfolioItem class for holdings tracking
  - Transaction class for trade history
- Includes proper error handling and input validation

## Sample Stocks Included
- AAPL (Apple Inc.)
- GOOGL (Alphabet Inc.)
- MSFT (Microsoft Corp.)
- AMZN (Amazon.com Inc.)
- TSLA (Tesla Inc.)
- NFLX (Netflix Inc.)
- META (Meta Platforms)
- NVDA (NVIDIA Corp.)

## Limitations
- Simulated market data (not real-time)
- Basic console interface
- Single-user only (no database persistence)

## Future Enhancements
- Add graphical user interface
- Connect to real market data API
- Implement user authentication
- Add portfolio persistence
- Support for more complex orders (limit, stop-loss)

## License
This project is open-source and available under the MIT License.
