import React, { useEffect, useState } from 'react'

export default function Profiles({ token, onLogout }){
  const [profiles, setProfiles] = useState([])
  const [msg, setMsg] = useState('')

  async function load(){
    setMsg('')
    try{
      const res = await fetch('/api/profiles', { headers: { Authorization: 'Bearer ' + token } })
      const j = await res.json()
      if (!res.ok) { setMsg(JSON.stringify(j)); return }
      setProfiles(j.results || [])
    }catch(e){ setMsg(String(e)) }
  }
  useEffect(()=>{ load() }, [])

  async function swipe(id, dir){
    await fetch(`/api/profiles/${id}/swipe`, { method:'POST', headers: {'Content-Type':'application/json', Authorization: 'Bearer ' + token }, body: JSON.stringify({ direction: dir }) })
    load()
  }

  return (
    <div>
      <div style={{marginBottom:10}}><button onClick={onLogout}>Logout</button> <button onClick={load}>Refresh</button></div>
      {msg && <div style={{color:'red'}}>{msg}</div>}
      <div>
        {profiles.map(p => (
          <div key={p.id} style={{border:'1px solid #ddd', padding:12, marginBottom:8, display:'flex', gap:12}}>
            <img src={(p.photos && p.photos[0]) || 'https://via.placeholder.com/120'} alt="photo" style={{width:120,height:120,objectFit:'cover'}} />
            <div>
              <h3>{p.name}, {p.age}</h3>
              <p>{p.bio}</p>
              <div>
                <button onClick={()=>swipe(p.id,'like')}>Like ❤️</button>
                <button onClick={()=>swipe(p.id,'dislike')}>Nope</button>
              </div>
            </div>
          </div>
        ))}
        {profiles.length===0 && <div>No more profiles</div>}
      </div>
    </div>
  )
}
