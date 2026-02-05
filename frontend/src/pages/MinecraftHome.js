import React, { useState } from 'react';
import './MinecraftHome.css';

const MinecraftHome = () => {
  const API_BASE = process.env.REACT_APP_API_BASE || '';
  const [searchInput, setSearchInput] = useState('');
  const [userData, setUserData] = useState(null);
  const [matchStats, setMatchStats] = useState(null);
  const [matchStatsByType, setMatchStatsByType] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [matchType, setMatchType] = useState(2); // Default to ranked

  const MATCH_TYPES = {
    1: { label: 'Casual'},
    2: { label: 'Ranked'},
    3: { label: 'Private'},
    4: { label: 'Event'},
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchInput.trim()) return;
    await fetchStats(searchInput.trim(), matchType);
  };

  const fetchStats = async (username, type) => {
    setLoading(true);
    setError(null);
    setUserData(null);
    setMatchStats(null);
    setMatchStatsByType({});

    try {
      // Fetch user data
      const userResponse = await fetch(`${API_BASE}/api/mcsr/users/${username}`);
      if (!userResponse.ok) throw new Error('User not found');
      const user = await userResponse.json();
      setUserData(user);

      // Fetch match statistics for all types up front
      const typeKeys = Object.keys(MATCH_TYPES);
      const statsResponses = await Promise.all(
        typeKeys.map((typeKey) =>
          fetch(`${API_BASE}/api/mcsr/users/${username}/stats?count=100&type=${typeKey}`)
            .then((response) => (response.ok ? response.json() : null))
            .then((stats) => [typeKey, stats])
        )
      );

      const statsMap = statsResponses.reduce((acc, [typeKey, stats]) => {
        if (stats) {
          acc[typeKey] = stats;
        }
        return acc;
      }, {});

      setMatchStatsByType(statsMap);
      setMatchStats(statsMap[type] || null);

      // Scroll to content
      setTimeout(() => {
        window.scrollTo({ top: 200, behavior: 'smooth' });
      }, 100);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Re-fetch when match type changes (if we have a user)
  const handleMatchTypeChange = (type) => {
    setMatchType(type);
    const cached = matchStatsByType[type];
    if (cached) {
      setMatchStats(cached);
    }
  };

  const formatTime = (ms) => {
    if (!ms) return 'N/A';
    const minutes = Math.floor(ms / 60000);
    const seconds = Math.floor((ms % 60000) / 1000);
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  const formatLabel = (value) => (value ? value.replace(/_/g, ' ') : 'UNKNOWN');

  const getStatByType = (stats, key, type) => {
    const byType = stats?.[key];
    if (!byType || type == null) {
      return null;
    }

    return byType[type] ?? null;
  };

  const bestTimeAllTime = getStatByType(matchStats, 'bestTimeAllTimeByType', matchType);
  const bestTimeLastMatches = getStatByType(matchStats, 'bestTimeLastMatchesByType', matchType);
  const averageTimeAllTime = getStatByType(matchStats, 'averageTimeAllTimeByType', matchType);
  const averageTimeLastMatches = getStatByType(matchStats, 'averageTimeLastMatchesByType', matchType);

  const seasonBestTimeRanked = userData?.statistics?.season?.bestTime?.ranked ?? null;
  const seasonAvgTimeRanked = (() => {
    const totalTime = userData?.statistics?.season?.completionTime?.ranked ?? null;
    const completions = userData?.statistics?.season?.completions?.ranked ?? null;
    if (!totalTime || !completions) {
      return null;
    }
    return totalTime / completions;
  })();

  const calculateSeasonWinRate = (user) => {
    const wins = user.statistics?.season?.wins?.ranked || 0;
    const losses = user.statistics?.season?.loses?.ranked || 0;
    const total = wins + losses;
    return total > 0 ? ((wins / total) * 100).toFixed(1) : 0;
  };

  const getTypeWinRate = (statsMap, typeKey) => {
    if (!statsMap || !typeKey || !statsMap[typeKey]) {
      return null;
    }

    return statsMap[typeKey].winRate ?? null;
  };

  const formatWinRate = (value) => (value == null ? 'N/A' : `${value.toFixed(1)}% WR`);

  // Keep forfeit rate tied to ranked season stats
  const getOverallForfeitStats = () => {
    const forfeits = userData?.statistics?.season?.forfeits?.ranked || 0;
    const matches = userData?.statistics?.season?.playedMatches?.ranked || 0;

    return {
      rate: matches > 0 ? ((forfeits / matches) * 100).toFixed(1) : 0
    };
  };

  return (
    <div className="minecraft-app">
      {/* Fixed Header */}
      <header className="minecraft-header">
        <div className="header-content">
          <div className="header-logo">
            <span className="logo-icon">️</span>
            <span className="logo-text-header">MCSR TRACKER</span>
          </div>

          <form className="header-search" onSubmit={handleSearch}>
            <input
              type="text"
              placeholder="Search Player..."
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              className="header-input"
              disabled={loading}
            />
            <button type="submit" className="header-button" disabled={loading}>
              {loading ? '...' : 'GO'}
            </button>
          </form>

        </div>
      </header>

      {error && (
        <div className="error-banner">
          ❌ {error}
        </div>
      )}

      {/* Main Content */}
      {userData ? (
        <main className="main-content">
          <div className="wide-container">
            {(() => {
              return (
                <>
            {/* Player Header */}
            <section className="player-header">
              <div className="player-title">
                <img
                  className="player-avatar"
                  src={`https://mineskin.eu/avatar/${encodeURIComponent(userData.nickname)}`}
                  alt={`${userData.nickname} avatar`}
                />
                <h1>{userData.nickname}</h1>
                {userData.country && (
                  <span className="country-flag">🌍 {userData.country.toUpperCase()}</span>
                )}
              </div>
              <div className="rank-badges">
                {userData.rankTier && (
                  <div className={`badge tier-badge tier-${userData.rankTier.color}`}>
                    <div className="badge-label">TIER</div>
                    <div className="badge-value">
                      {userData.rankTier.icon} {userData.rankTier.name}
                      {userData.rankTier.division && (
                        <span className="division"> {userData.rankTier.division}</span>
                      )}
                    </div>
                  </div>
                )}
                <div className="badge rank-badge">
                  <div className="badge-label">RANK</div>
                  <div className="badge-value">#{userData.eloRank}</div>
                </div>
                <div className="badge elo-badge">
                  <div className="badge-label">ELO</div>
                  <div className="badge-value">{userData.eloRate}</div>
                </div>
                <div className="badge time-badge">
                  <div className="badge-label">SEASON BEST</div>
                  <div className="badge-value">{formatTime(seasonBestTimeRanked)}</div>
                </div>
                <div className="badge time-badge">
                  <div className="badge-label">SEASON AVG</div>
                  <div className="badge-value">{formatTime(seasonAvgTimeRanked)}</div>
                </div>
              </div>
              <div className="player-season-stats stats-grid-wide">
                <div className="stat-box wins">
                  <div className="stat-value">{userData.statistics?.season?.wins?.ranked || 0}</div>
                  <div className="stat-label">Season Wins</div>
                </div>
                <div className="stat-box losses">
                  <div className="stat-value">{userData.statistics?.season?.loses?.ranked || 0}</div>
                  <div className="stat-label">Season Losses</div>
                </div>
                <div className="stat-box winrate">
                  <div className="stat-value">{calculateSeasonWinRate(userData)}%</div>
                  <div className="stat-label">Season Win Rate</div>
                </div>
                <div className="stat-box matches">
                  <div className="stat-value">{userData.statistics?.season?.playedMatches?.ranked || 0}</div>
                  <div className="stat-label">Season Matches</div>
                </div>
                <div className="stat-box streak">
                  <div className="stat-value">{userData.statistics?.season?.highestWinStreak?.ranked || 0}</div>
                  <div className="stat-label">Season Streak</div>
                </div>
                <div className="stat-box forfeit">
                  <div className="stat-value">{getOverallForfeitStats()?.rate || 0}%</div>
                  <div className="stat-label">Season Forfeit Rate</div>
                </div>
              </div>
                  </section>

                </>
              );
            })()}

            {/* Match Type Filter */}
            <section className="match-type-filter">
              <span className="filter-label">Match Type:</span>
              <div className="filter-buttons">
                {Object.entries(MATCH_TYPES).map(([type, { label}]) => (
                  <button
                    key={type}
                    className={`filter-btn ${parseInt(type) === matchType ? 'active' : ''}`}
                    onClick={() => handleMatchTypeChange(parseInt(type))}
                    disabled={loading}
                  >
                    <span className="filter-text">{label}</span>
                  </button>
                ))}
              </div>
            </section>

            {/* Match Statistics - PRIORITY */}
            {matchStats && (
              <>
                <section className="stats-section">
                  <h2 className="section-header">
                   MATCH ANALYSIS — {MATCH_TYPES[matchType]?.icon} {MATCH_TYPES[matchType]?.label.toUpperCase()} (LAST {matchStats.totalMatches || 0} GAMES)
                  </h2>
                  <div className="time-stats-grid">
                    <div className="time-stats-row time-stats-row-single">
                      {bestTimeLastMatches != null ? (
                        <div className="stat-box time-last">
                          <div className="stat-value">{formatTime(bestTimeLastMatches)}</div>
                          <div className="stat-label">Best Time (Last 100)</div>
                        </div>
                      ) : (
                        <div className="stat-box time placeholder" />
                      )}
                      {averageTimeLastMatches != null ? (
                        <div className="stat-box time-last">
                          <div className="stat-value">{formatTime(averageTimeLastMatches)}</div>
                          <div className="stat-label">Avg Time (Last 100)</div>
                        </div>
                      ) : (
                        <div className="stat-box time placeholder" />
                      )}
                    </div>
                  </div>

                  <h3 className="subsection-header">BASTION TYPE BREAKDOWN</h3>
                  <div className="breakdown-grid">
                    {Object.entries(matchStats.bastionStats).map(([type, data]) => (
                      <div key={type} className="breakdown-card">
                        <div className="breakdown-title">{formatLabel(type)}</div>
                        <div className="breakdown-stats">
                          <div className="breakdown-row">
                            <span>Winrate:</span>
                            <span className="val">{data.winRate.toFixed(0)}%</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Record:</span>
                            <span className="val">{data.wins}W-{data.losses}L</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Finishes:</span>
                            <span className="val finish-count">{data.finishedWins || 0}</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Average:</span>
                            <span className="val">{formatTime(data.averageWinTime)}</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Best:</span>
                            <span className="val best-time">{formatTime(data.fastestTime)}</span>
                          </div>
                          {(data.forfeitLosses > 0 || data.forfeitWins > 0) && (
                            <div className="breakdown-row forfeit-row">
                              <span>User FF:</span>
                              <span className="val forfeit-loss-val">{data.forfeitLosses || 0}</span>
                            </div>
                          )}
                          {(data.forfeitLosses > 0 || data.forfeitWins > 0) && (
                            <div className="breakdown-row">
                              <span>Opponent FF:</span>
                              <span className="val forfeit-win-val">{data.forfeitWins || 0}</span>
                            </div>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>

                  <h3 className="subsection-header">SEED TYPE BREAKDOWN</h3>
                  <div className="breakdown-grid">
                    {Object.entries(matchStats.seedStats).map(([type, data]) => (
                      <div key={type} className="breakdown-card">
                        <div className="breakdown-title">{formatLabel(type)}</div>
                        <div className="breakdown-stats">
                          <div className="breakdown-row">
                            <span>Winrate:</span>
                            <span className="val">{data.winRate.toFixed(0)}%</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Record:</span>
                            <span className="val">{data.wins}W-{data.losses}L</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Finishes:</span>
                            <span className="val finish-count">{data.finishedWins || 0}</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Average:</span>
                            <span className="val">{formatTime(data.averageWinTime)}</span>
                          </div>
                          <div className="breakdown-row">
                            <span>Best:</span>
                            <span className="val best-time">{formatTime(data.fastestTime)}</span>
                          </div>
                          {(data.forfeitLosses > 0 || data.forfeitWins > 0) && (
                            <div className="breakdown-row forfeit-row">
                              <span>User FF:</span>
                              <span className="val forfeit-loss-val">{data.forfeitLosses || 0}</span>
                            </div>
                          )}
                          {(data.forfeitLosses > 0 || data.forfeitWins > 0) && (
                            <div className="breakdown-row">
                              <span>Opponent FF:</span>
                              <span className="val forfeit-win-val">{data.forfeitWins || 0}</span>
                            </div>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>

                </section>

                {(matchStats.bestSeed || matchStats.worstSeed || matchStats.bestBastion || matchStats.worstBastion) && (
                  <section className="stats-section">
                    <h3 className="subsection-header">BEST & WORST TYPES</h3>
                    <div className="stats-grid-wide stats-grid-paired">
                      {matchStats.bestSeed && (
                        <div className="stat-box best">
                          <div className="stat-value">{formatLabel(matchStats.bestSeed)}</div>
                          <div className="stat-subvalue">
                            {formatWinRate(getTypeWinRate(matchStats.seedStats, matchStats.bestSeed))}
                          </div>
                          <div className="stat-label">Best Seed</div>
                        </div>
                      )}
                      {matchStats.worstSeed && (
                        <div className="stat-box worst">
                          <div className="stat-value">{formatLabel(matchStats.worstSeed)}</div>
                          <div className="stat-subvalue">
                            {formatWinRate(getTypeWinRate(matchStats.seedStats, matchStats.worstSeed))}
                          </div>
                          <div className="stat-label">Worst Seed</div>
                        </div>
                      )}
                      {matchStats.bestBastion && (
                        <div className="stat-box best">
                          <div className="stat-value">{formatLabel(matchStats.bestBastion)}</div>
                          <div className="stat-subvalue">
                            {formatWinRate(getTypeWinRate(matchStats.bastionStats, matchStats.bestBastion))}
                          </div>
                          <div className="stat-label">Best Bastion</div>
                        </div>
                      )}
                      {matchStats.worstBastion && (
                        <div className="stat-box worst">
                          <div className="stat-value">{formatLabel(matchStats.worstBastion)}</div>
                          <div className="stat-subvalue">
                            {formatWinRate(getTypeWinRate(matchStats.bastionStats, matchStats.worstBastion))}
                          </div>
                          <div className="stat-label">Worst Bastion</div>
                        </div>
                      )}
                    </div>
                  </section>
                )}
              </>
            )}

            {/* Connections - Lower Priority */}
            {userData.connections && (userData.connections.discord || userData.connections.twitch || userData.connections.youtube) && (
              <section className="stats-section">
                <h2 className="section-header">🔗 CONNECTIONS</h2>
                <div className="connections-row">
                  {userData.connections.discord && (
                    <div className="connection-item">
                      <span className="conn-icon"></span>
                      <div className="conn-info">
                        <div className="conn-label">Discord</div>
                        <div className="conn-value">{userData.connections.discord.name}</div>
                      </div>
                    </div>
                  )}
                  {userData.connections.twitch && (
                    <a
                      href={`https://twitch.tv/${userData.connections.twitch.id}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="connection-item"
                    >
                      <span className="conn-icon">📺</span>
                      <div className="conn-info">
                        <div className="conn-label">Twitch</div>
                        <div className="conn-value">{userData.connections.twitch.name}</div>
                      </div>
                    </a>
                  )}
                  {userData.connections.youtube && (
                    <a
                      href={`https://youtube.com/channel/${userData.connections.youtube.id}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="connection-item"
                    >
                      <span className="conn-icon"></span>
                      <div className="conn-info">
                        <div className="conn-label">YouTube</div>
                        <div className="conn-value">{userData.connections.youtube.name}</div>
                      </div>
                    </a>
                  )}
                </div>
              </section>
            )}
          </div>
        </main>
      ) : (
        <div className="welcome-section">
          <div className="welcome-content">
            <h1 className="welcome-title">MCSR RANKED TRACKER</h1>
            <p className="welcome-text">Search for a player above to view their stats and match analysis</p>
            <div className="welcome-features">
              <div className="feature">Season & All-Time Stats</div>
              <div className="feature">Bastion Performance Analysis</div>
              <div className="feature">Seed Type Breakdown</div>
              <div className="feature">Last 100 Matches Analyzed</div>
            </div>
          </div>
        </div>
      )}

      {/* Footer */}
      <footer className="minecraft-footer">
        <p>Data from MCSR Ranked API • <a href="https://mcsrranked.com" target="_blank" rel="noopener noreferrer">Official Site</a></p>
      </footer>
    </div>
  );
};

export default MinecraftHome;
