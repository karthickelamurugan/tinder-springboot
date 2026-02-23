// Simple smoke test script using node-fetch
// Requires server running at http://localhost:8080

const fetch = require('node-fetch');

async function run() {
  const base = 'http://localhost:8080';
  console.log('Seeding...');
  await fetch(base + '/api/admin/seed', { method: 'POST' });

  console.log('Logging in as alice');
  const loginRes = await fetch(base + '/api/auth/login', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ username:'alice', password:'password' }) });
  if (!loginRes.ok) { console.error('login failed', await loginRes.text()); process.exit(2); }
  const { token } = await loginRes.json();
  console.log('token length', token.length);

  console.log('Fetching profiles');
  const profiles = await (await fetch(base + '/api/profiles', { headers: { Authorization: 'Bearer ' + token } })).json();
  console.log('profiles', profiles.results ? profiles.results.length : profiles);

  if (!profiles.results || profiles.results.length === 0) { console.error('no profiles'); process.exit(3); }

  const first = profiles.results[0];
  console.log('Swiping like on', first.id);
  const swipe = await (await fetch(base + `/api/profiles/${first.id}/swipe`, { method:'POST', headers:{'Content-Type':'application/json', Authorization: 'Bearer ' + token}, body: JSON.stringify({ direction: 'like' }) })).json();
  console.log('swipe result', swipe);

  console.log('Done smoke');
}

run().catch(err => { console.error(err); process.exit(1); });
