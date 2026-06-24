import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";

export default function VehicleShowcase() {
  const targetRef = useRef(null);
  const { scrollYProgress } = useScroll({ 
    target: targetRef,
    offset: ["start end", "end start"]
  });
  
  const scale = useTransform(scrollYProgress, [0, 1], [0.9, 1.05]);
  const y = useTransform(scrollYProgress, [0, 1], ["10%", "-10%"]);

  return (
    <section id="models" ref={targetRef} className="relative py-32 md:py-48 bg-surface-charcoal overflow-hidden">
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-30 mb-16 md:mb-32 text-center">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
        >
          <span className="text-secondary font-bold tracking-widest text-sm uppercase mb-4 inline-block">The Flagship</span>
          <h2 className="font-headline-xl text-5xl md:text-7xl text-primary leading-tight">
            The Future of Urban Transit.
          </h2>
          <p className="text-text-secondary mt-8 text-xl md:text-2xl font-light max-w-3xl mx-auto leading-relaxed">
            Reimagining the icon of Indian cities for a sustainable, zero-emission tomorrow. Engineered for Bengaluru's streets.
          </p>
        </motion.div>
      </div>

      <div className="relative w-full h-[60vh] md:h-[80vh] flex items-center justify-center">
        <motion.div style={{ scale, y }} className="relative w-full max-w-6xl h-full px-4">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,_var(--tw-gradient-stops))] from-secondary/10 via-transparent to-transparent opacity-80 z-0"></div>
          
          <img 
            src="https://lh3.googleusercontent.com/aida-public/AB6AXuCvkG4PN51kxrHtC2xiHZINplvQUMfusf1ZhNn4m9-wpwevkZpkUARNdMN9wDjYS7KtR_ArZRmEZ2nUaxKzP6inukVrDNLvFmhDsnSQSGqO78Se0yJi_ruEEA3hEbenOogSLKHhBnoLvuQ6FGh2DFWF-XKAX6-fEWh-wrw_dAIw27cDCsxPlocdqdP8ZjFKjxEG201RyoURAAvMFZuuUjH28jKnjt43RaQI8NKfLUr7QlN-zwjkus8IkvkvzMyUYmrz51NnsYAz_HWd" 
            className="w-full h-full object-contain relative z-10 drop-shadow-[0_20px_50px_rgba(0,0,0,0.5)]" 
            alt="EV Auto" 
          />
          
          {/* Floating Stats */}
          <motion.div 
            initial={{ opacity: 0, x: -50 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ delay: 0.3 }}
            className="absolute top-[10%] md:top-[20%] left-4 md:left-[10%] glass-panel p-4 md:p-6 rounded-2xl z-20 backdrop-blur-md border border-glass-border/30 bg-background/80"
          >
            <div className="text-secondary font-bold text-3xl md:text-5xl mb-1">180km</div>
            <div className="text-xs text-text-secondary uppercase tracking-widest">Real-World Range</div>
          </motion.div>

          <motion.div 
            initial={{ opacity: 0, x: 50 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ delay: 0.5 }}
            className="absolute bottom-[10%] md:bottom-[20%] right-4 md:right-[10%] glass-panel p-4 md:p-6 rounded-2xl z-20 backdrop-blur-md border border-glass-border/30 bg-background/80"
          >
            <div className="text-secondary font-bold text-3xl md:text-5xl mb-1">Zero</div>
            <div className="text-xs text-text-secondary uppercase tracking-widest">Tailpipe Emissions</div>
          </motion.div>
        </motion.div>
      </div>
    </section>
  );
}
